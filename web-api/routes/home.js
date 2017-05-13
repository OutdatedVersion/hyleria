/** Ben (OutdatedVersion) | Apr/28/2017 (8:31 PM) */

/**
 * Responds to the root (/) with basic
 * information regarding the API.
 *
 * @param server - Restify instance
 */
export default (server) =>
{
    server.get('/', (req, res) =>
    {
        res.json({
            message: `there isn't much here; perhaps seek help via our docs`,
            documentation_url: server.data.info.documentation_url
        })
    })
}