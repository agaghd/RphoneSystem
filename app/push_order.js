var http = require('http');
var url = require('url');
var util = require('util');
var JPush = require("./node_modules/jpush-async/lib/JPush/JPushAsync.js")
var client = JPush.buildClient('2f9a6d3784085150e3c25472', '477e89b4dd69fd29fda679ce')

var pushOrder = function(json, tag, response){
	json = JSON.stringify(json)
	client.push().setPlatform('android')
    .setAudience(JPush.tag(tag))
    .setNotification('Server Order', JPush.android(json, 'Server Order', 1))
    .setMessage(json)
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
   
	var body = ""
	req.on('data', function(chunk){
		body += chunk
	})
	req.on('end', function(){
		console.log("body: " + body)
		res.writeHead(200, {'Content-Type': 'text/plain'})
		body = JSON.parse(body)
		console.log("order: " + body.order)
		console.log("tag: " + body.tag)
		pushOrder(body, body.tag,res)
		res.end()
	})
}).listen(9090);
