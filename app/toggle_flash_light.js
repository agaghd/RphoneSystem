var http = require('http');
var url = require('url');
var util = require('util');
var JPush = require("./node_modules/jpush-async/lib/JPush/JPushAsync.js")
var client = JPush.buildClient('2f9a6d3784085150e3c25472', '477e89b4dd69fd29fda679ce')

var toggleFlashLight = function(order, tag, response){
	order = String(order)
	tag = String(tag)
	client.push().setPlatform('android')
    .setAudience(JPush.tag(tag))
    .setNotification('Server Order', JPush.android(order, 'Server Order', 1))
    .setMessage(order)
    .setOptions(null, 60)
    .send()
    .then(function(result) {
        console.log(result)
		response.end('success')
    }).catch(function(err) {
        console.log(err)
		response.end('err')
    })
}

http.createServer(function(req, res){
    res.writeHead(200, {'Content-Type': 'text/plain'})
	// 解析 url 参数
	console.log(req.url)
    var params = url.parse(req.url, true).query
	toggleFlashLight(params.order, params.target, res)
}).listen(9090);
