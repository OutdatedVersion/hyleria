/** Ben (OutdatedVersion) | May/10/2017 (4:45 PM) */

import { InvalidArgumentError } from 'restify/lib/errors'
import util from '../util'

export default (req, res, next) =>
{
    console.log(req.params)

    // pattern validation
    // > non null/empty
    // > username: [A-Za-z0-9_{1,16}]
    // > uuid: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}

    if (req.params.id === '')
    {
        res.send(new InvalidArgumentError(`No name/UUID was found on your request`))
        return
    }

    // everything checks out, continue request cycle
    next()
}