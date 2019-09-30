var JPush = require("../node_modules/jpush-async/lib/JPush/JPushAsync.js")
var client = JPush.buildClient('2f9a6d3784085150e3c25472', '477e89b4dd69fd29fda679ce')

var push = function (body, res) {
	console.log("body: " + body)
	json = JSON.parse(body)
	tag = json.tag
	console.log("tag: " + tag)
	client.push().setPlatform('android')
		.setAudience(JPush.tag(tag))
		.setNotification('Server Order', JPush.android(body, 'Server Order', 1))
		.setMessage(body)
		.setOptions(null, 60)
		.send()
		.then(function (result) {
			console.log(result)
			res.end('success')
		}).catch(function (err) {
			console.log(err)
			res.end('err')
		})
}

exports.push = push
