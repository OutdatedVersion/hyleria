/** Ben (OutdatedVersion) | May/10/2017 (4:45 PM) */

import { InvalidArgumentError, PreconditionFailedError } from 'restify'

const combinedPattern = new RegExp( [ /A-Za-z0-9_{1,16}/,
                                      /[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}/,
                                      /[0-9a-f]{32}/ ].join('|'), 'i')


/**
 * Middleware item for checking whether
 * the requested item does in fact match
 * the requirements for a 'key'.
 *
 * @param req - Request object
 * @param res - Response object
 * @param next - Cycle advancement
 */
export default (req, res, next) =>
{
    const id = req.params.id

    if (id === '')
    {
        res.send(new InvalidArgumentError('No name/UUID was found on your request'))
        return
    }

    if (!id.match(combinedPattern))
    {
        res.send(new PreconditionFailedError(`'${id}' did not match any valid pattern! [undashed UUID, dashed UUID, username]`))
        return
    }

    // everything checks out, continue request cycle
    next()
}