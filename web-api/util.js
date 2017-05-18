/** Ben (OutdatedVersion) | Apr/29/2017 (11:20 PM) */

import assert from 'assert'
import Debug from 'debug'
import * as config from './config.json'


/**
 * Pretty console printing
 */
const debug = Debug('api:server')

/**
 * Removes the dashes in a UUID
 *
 * @param uuid the unique id for a player
 * @returns {string} the uuid but stripped of dashes
 */
let undash = (uuid) =>
{
    assert(typeof uuid === 'string', 'uuid must be provided as a string')

    return uuid.replace(/-/g, '')
}

/**
 * Add the dashes back into a
 * naked UUID
 *
 * @param val the plain UUID
 * @returns {string} the freshly dashed unique id
 */
let dash = (val) =>
{
    assert(typeof val === 'string', 'we need to work with a string')

    // ok well this is confusing
    return val.substr(0, 8) + '-' + val.substr(8, 11) + '-' + val.substr(11, 18) + '-' + val.substr(19, 23) + '-' + val.substr(24, 36)
}

/**
 * Checks whether the provided String
 * is a valid UUID
 *
 * @param val the string
 * @returns {boolean} yes or no
 */
let isUUID = (val) =>
{
    assert(typeof val === 'string', 'we can only verify a string')

    return val.match(/^[0-9A-F]{8}-[0-9A-F]{4}-[4][0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}$/i)
}

/**
 * Checks whether the provided String
 * is a valid Minecraft username
 *
 * @param val the string
 * @returns {boolean} yes or no
 */
let isValidUsername = (val) =>
{
    assert(typeof val === 'string', 'we can only verify a string')

    return val.match(/[a-zA-Z0-9_]{1,16}$/)
}


export default {
    debug,
    undash,
    dash,
    isUUID,
    isValidUsername,
    config
}