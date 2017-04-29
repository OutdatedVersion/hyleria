/** Ben (OutdatedVersion) | Apr/28/2017 (8:31 PM) */

import restify from 'restify'
import routes from './routes'

const server = restify.createServer({ name: 'hyleria-api' })
routes(server)

// we don't need rate limiting quite yet
// server.use(restify.throttle())

server.listen(3000)

// export for testing
module.exports = server
