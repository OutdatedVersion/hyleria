/** Ben (OutdatedVersion) | Apr/29/2017 (1:03 AM) */

import validation from '../middleware/request-validation'
import data from '../data'

export default (server) =>
{
    server
        .use(validation)
        .get('/player/:id', (req, res) =>
    {
        data.grabPlayer(req.params.id).then(result =>
        {
            res.json(result.data || { err: `unable to fetch data for ${req.params.id}` })
        })
        .catch(error =>
        {
            res.json({ err: error })
        })
    })
}