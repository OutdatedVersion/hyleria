const router = require('express').Router()

router.get('/', (req, res) =>
{
    res.json({ 'message': 'hello there :)' })
})


module.exports = router
