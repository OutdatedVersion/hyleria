/** Ben (OutdatedVersion) | Apr/29/2017 (11:20 PM) */

import assert from 'assert'


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


export default {
    undash,
    dash
}