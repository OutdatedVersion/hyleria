/** Ben (OutdatedVersion) | May/10/2017 (4:45 PM) */

import { InvalidArgumentError } from 'restify/lib/errors'
import util from '../util'

import restify from 'restify'

export default (req, res, next) =>
{
    console.log(req.params)

    if (req.params.id === '')
    {
        res.send(new InvalidArgumentError(`No name/UUID was found on your request`))
        return
    }


    next() // request will hang w/o
}