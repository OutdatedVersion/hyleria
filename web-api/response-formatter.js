/** Ben (OutdatedVersion) | May/10/2017 (5:21 PM) */

import * as config from './config.json'

/**
 * Respond to some web request with the data
 * that was asked of us. Along with some
 * indication like fields.
 *
 * @param res - The Result object of web requests
 * @param startedAt - When we started processing the request
 * @param data - Data we're sending out
 */
let ok = (res, startedAt, data) =>
{
    res.json({
        status: config.status.ok,
        elapsed_time: startedAt - Date.now(),
        data
    })
}

/**
 * Respond to a certain request with an indication
 * that something went wrong.
 *
 * @param res - The Result object from the request
 * @param err - The error that occurred
 */
let err = (res, err) =>
{
   res.json({
       status: config.status.failure,
       code: err.cause,
       message: err.message
   })
}


export default {
    err,
    ok
}