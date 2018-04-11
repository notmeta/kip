# kip
A ShareX file upload and hosting server written in Kotlin targeting the JVM.  

<hr>

### Future plans:
- Secret token/password system
- Custom image directories, i.e. 'http://coreys.xyz/screenshots/image.png' vs 'http://coreys.xyz/image.png'
- URL Shortening service
- Autogenerate .sxcu file (*and/or json input string*) to import into sharex automatically
- Documentation
- Commands to run while server is live, i.e. download zip of all currently uploaded images, restart server
- Option to drop file extension on the return link, i.e. 'http://coreys.xyz/image.png' -> 'http://coreys.xyz/image'

<hr>  

### Examples:  
#### config.json
```json
{
  "port": 80,
  "uploadDirectory": "upload",
  "uploadUrl": "upload",
  "formName": "sharex",
  "siteUrl": "http://coreys.xyz/",
  "keepFileName": false
}
```
#### Output:
http://coreys.xyz/w6z1jH.png  
http://coreys.xyz/hWIh7f.txt  

<hr>

### References and credits:
- http://sparkjava.com/
- https://getsharex.com/docs/custom-uploader
- https://github.com/aerouk/imageserve
