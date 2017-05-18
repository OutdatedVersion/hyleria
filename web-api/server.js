/** Ben (OutdatedVersion) | Apr/28/2017 (8:31 PM) */

import restify from 'restify'
import routes from './routes'
import util from './util'

util.debug('initializing server & registering routes')

const server = restify.createServer({ name: 'hyleria-api' })

// allow access to our data all around
server.data = util.config

// setup web routes
routes(server)

// we don't need rate limiting quite yet
// server.use(restify.throttle())

server.listen(process.env.port || 3000)
util.debug(`now listening on 127.0.0.1:${server.address().port}`)


export default server