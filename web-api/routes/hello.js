/** Ben (OutdatedVersion) | Apr/28/2017 (8:31 PM) */

export default (server) =>
{
  server.get('/hello/:name', (req, res, next) =>
  {
    res.send(`hello ${req.params.name}`)
    return next()
  })
}
