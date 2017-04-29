/** Ben (OutdatedVersion) | Apr/28/2017 (8:31 PM) */

export default (server) =>
{
    server.get('/', (req, res, next) =>
    {
        res.json({ message: `there isn't much here; perhaps seek help via our docs`,
                   documentation_url: 'https://github.com/OutdatedVersion/hyleria' })

        return next()
    })
}