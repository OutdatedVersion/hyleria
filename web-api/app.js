const express = require('express'),
         path = require('path'),
       logger = require('morgan'),
   bodyParser = require('body-parser')


const index = require('./routes/index')


const app = express()

app.use(logger('dev'))
app.use(bodyParser.json())
app.use(bodyParser.urlencoded({ extended: false }))

app.use('/', index)


// process 404s
app.use((req, res) =>
{
    res.status(404).json({ 'message': 'no route found matching request', success: false })
    // consider adding documentation link here
})


// error handler
app.use((err, req, res, next) =>
{
    let send = {}

    send.message = err.message
    send.detailed = req.app.get('env') === 'development' ? err : ''

    res.status(err.status || 500).json(send)
})


module.exports = app
