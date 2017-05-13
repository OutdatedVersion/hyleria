/** Ben (OutdatedVersion) | Apr/28/2017 (8:31 PM) */

import home from './home'
import player from './player'

/**
 * A central place to start
 * the registration of web routes.
 *
 * @param server - Restify instance
 */
export default (server) =>
{
    home(server)
    player(server)
}
