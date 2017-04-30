/** Ben (OutdatedVersion) | Apr/28/2017 (8:31 PM) */

// combine routes
import home from './home'
import player from './player'

export default (server) =>
{
    home(server)
    player(server)
}
