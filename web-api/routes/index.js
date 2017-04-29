/** Ben (OutdatedVersion) | Apr/28/2017 (8:31 PM) */

// combine routes
import hello from './hello'
import home from './home'

export default (server) =>
{
    hello(server)
    home(server)
}
