The primary goal of bulba-scrape-maven-plugin is to be able to generate JSON files that include relevant information about cards and expansions for the ptcg web app during the generate-resources phase.

This can be dissolved into the following steps.

* (DONE) Be able to cache resources to file system.
* (DONE) Download and cache resources from Bulbapedia.
* Scrape documents to discover what resources to download.
* Scrape documents to extract expansion information.
* Scrape documents to extract card information.
