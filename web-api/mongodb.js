/** Ben (OutdatedVersion) | May/14/2017 (8:20 PM) */

import { MongoClient } from 'mongodb'

/**
 * Connect to our database and return
 * a connection pool
 *
 * @param options - Object containing the
 *                  details we require
 * @return {Promise} - Something wrapping our fresh
 *                     pool instance that you're given
 */
export default (options) =>
{
    Object.assign(options, {
        host: '127.0.0.1',
        port: 27017,
        database: 'default_database'
    })

    return new Promise((resolve, reject) =>
    {
        const uri = `mongodb://${options.host}:${options.port}/${options.database}`

        MongoClient.connect(uri).then(resolve).catch(reject)
    })
}