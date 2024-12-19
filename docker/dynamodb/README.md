## How to build on MAC

`docker build . --log-level=debug --platform linux/arm64`

## how to build for other platforms

`docker build . --log-level=debug --platform linux/amd64`

Don't forget to change the node links to amd64 variant