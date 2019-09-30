var http = require('http')
var url = require('url')
var util = require('util')
var router = require('./router')

http.createServer(function (req, res) {
    var pathname = url.parse(req.url).pathname
    console.log("url: " + url)
    var body = ""
    req.on('data', function (chunk) {
        body += chunk
    })
    req.on('end', function () {
        res.writeHead(200, { 'Content-Type': 'text/plain' })
        var method = router.route(pathname)
        console.log("method: " + method.name)
        if (method) {
            try {
                method(body, res)
            } catch (error) {
                console.error(error)
                res.end(error.toString())
            }

        }
    })
}).listen(9090);