/** Ben (OutdatedVersion) | May/10/2017 (4:45 PM) */

import { InvalidArgumentError, PreconditionFailedError } from 'restify'

const allPatterns = [ /[A-Za-z0-9_]{1,16}$/,
                      /[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/,
                      /[0-9a-f]{32}$/ ]


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


    let err = {}

    for (let pattern of allPatterns)
    {
        if (id.match(pattern))
            break
        else
            err.yes = pattern
    }


    if (err.yes)
    {
        res.send(new PreconditionFailedError(`'${id}' did not match a required pattern! [${err.yes}]`))
        return
    }

    // everything checks out, continue request cycle
    next()
}