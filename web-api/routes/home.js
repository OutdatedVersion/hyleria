/** Ben (OutdatedVersion) | Apr/28/2017 (8:31 PM) */

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