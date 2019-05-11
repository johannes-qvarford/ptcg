package main

import (
	"encoding/base64"
	"flag"
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
	"regexp"
)

// base url for all expansions https://bulbapedia.bulbagarden.net/wiki/List_of_Pok%C3%A9mon_Trading_Card_Game_expansions
// base url for base set https://bulbapedia.bulbagarden.net/wiki/Base_Set_(TCG)

func main() {
	cacheDir := flag.String("cache-directory", ".cache", "directory to store scraped data.")

	flag.Parse()

	prepareCacheDir(*cacheDir)
	cards, err := readCardsForSet(*cacheDir, "Base_Set")
	if err != nil {
		fmt.Fprint(os.Stderr, err)
		os.Exit(1)
	}

	fmt.Println(cards)
}

func readCardsForSet(cacheDir, setName string) ([]string, error) {
	urlPath := fmt.Sprintf("/wiki/%s_(TCG)", setName)
	body, err := readAndCacheDocument(cacheDir, urlPath)
	bodyStr := string(body)

	patternStr := fmt.Sprintf("/wiki/(.*_\\(%s_[0-9]+\\))", setName)
	matchCardLinks, err := regexp.CompilePOSIX(patternStr)
	if err != nil {
		return nil, err
	}

	subMatches := matchCardLinks.FindAllStringSubmatch(bodyStr, -1)
	cards := make([]string, len(subMatches))
	for i, subMatch := range subMatches {
		cards[i] = subMatch[1]
	}

	return cards, nil
}

func readAndCacheDocument(cacheDir, urlPath string) ([]byte, error) {

	base64Path := base64.StdEncoding.EncodeToString([]byte(urlPath))
	documentCachePath := fmt.Sprintf("%s/%s", cacheDir, base64Path)

	_, err := os.Stat(documentCachePath)
	if err == nil {
		return ioutil.ReadFile(documentCachePath)
	} else if err != nil && !os.IsNotExist(err) {
		return nil, err
	}

	document, err := http.Get("https://bulbapedia.bulbagarden.net/wiki/Base_Set_(TCG)")
	if err != nil {
		return nil, err
	}

	body, err := ioutil.ReadAll(document.Body)
	if err != nil {
		return nil, err
	}

	err = ioutil.WriteFile(documentCachePath, body, 0600)
	if err != nil {
		return nil, err
	}

	return ioutil.ReadFile(documentCachePath)
}

func prepareCacheDir(path string) error {
	_, err := os.Stat(path)
	if err != nil && os.IsNotExist(err) {
		return os.Mkdir(path, 0700)
	}
	return err
}
