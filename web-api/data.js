/** Ben (OutdatedVersion) | Apr/29/2017 (10:23 PM) */

import RedisClient from 'ioredis'
import { NotFoundError, InvalidContentError } from 'restify'
import Mongo from './mongodb'
import util from './util'


util.debug('connecting to redis')
const redis = new RedisClient()

util.debug('connecting to mongo')
const mongo = Mongo(util.config.mongo)


/**
 *
 * @param key
 * @returns {Promise}
 */
let grabPlayer = (key) =>
{
    return new Promise((fulfill, reject) =>
    {
        // redis -> mongo
        if (typeof key !== 'string')
            throw new TypeError('the key must be provided as a string')

        // we may use either a user's UUID or name to look them up
        let providedUUID = util.isUUID(key)

        // temp
        let uuid = key || '03c337cd7be04694b9b0e2fd03f57258'

        redis.get('api:player:' + uuid).then(result =>
        {
            if (result)
            {
                fulfill(JSON.parse(result))
            }
            else
            {


                reject(new NotFoundError(`No player found matching '${key}'`))
            }
        }).catch(err => reject(err))
    })
}


export default {
    grabPlayer
}