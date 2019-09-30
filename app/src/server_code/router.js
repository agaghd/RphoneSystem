var push_order = require('./methods/push_order')

var route = function (pathname) {
    console.log("pathname: " + pathname)
    switch (pathname) {
        case ('/push_order'):
            return push_order.push
        default:
            return null
    }

}
exports.route = route