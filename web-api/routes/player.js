/** Ben (OutdatedVersion) | Apr/29/2017 (1:03 AM) */

import validation from '../middleware/request-validation'
import format from '../response-formatter'
import data from '../data'

/**
 * In charge of processing requests for
 * basic player data.
 *
 * A typical request cycle is as follows:
 *
 * 1. Middleware validates
 *    rate limits and does basic validation
 *    on the player's identifier. Whether it
 *    be a username or UUID.
 * 2. Hit up Redis instance for a cache check.
 *    From here we'll either format then serve
 *    the request, or continue.
 * 3. Hit Mongo instance for the data then cache it.
 *
 * @param server - Restify instance
 */
export default (server) =>
{
    server
        .use(validation)
        .get('/player/:id', (req, res) =>
    {
        // TODO(Ben): use our response formatting utility for uniform responses everywhere.

        data.grabPlayer(req.params.id).then(result =>
        {
            //format.ok(res, )
            res.json(result.data)
        })
        .catch(error =>
        {
            res.json({ err: error })
        })
    })
}