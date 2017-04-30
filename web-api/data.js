/** Ben (OutdatedVersion) | Apr/29/2017 (10:23 PM) */

import Redis from 'ioredis'
import { MongoClient } from 'mongodb'
import util from './util'


util.debug('connecting to redis')
const rc = new Redis()


let grabPlayer = (key) =>
{
    return new Promise((fulfill, reject) =>
    {
        // redis -> mongo
        if (typeof key !== 'string')
            throw new TypeError('the key must be provided as a string')

        // we may use either a user's UUID or name to look
        // them up so we need to validate that we are using
        // one or the other
        let providedUUID = util.isUUID(key)

        if (!providedUUID && !util.isValidUsername(key))
            reject('invalid username provided')

        // temp
        let uuid = key || '03c337cd7be04694b9b0e2fd03f57258'

        rc.get('api:p:' + uuid).then((result) =>
        {
            if (result)
            {
                fulfill({ data: JSON.parse(result) })
            }

            reject('not found')
        })
    })
}


export default {
    grabPlayer
}