const axios = require('axios')

const RoomService = require('./RoomService')
const OrderItemService = require('./OrderItemService')
const KitchenService = require('./KitchenService')

var listRooms = []

class SocketService {
  connection(socket) {
    socket.use((packet, next) => {
      global.instanceAxios = axios.create({
        timeout: 10000,
        withCredentials: true,
      })
      next()
    })

    // console.log('Connected: ' + socket.id)
    // console.log("rooms ========");
    // console.log(socket.adapter.rooms);

    socket.emit('server-connection', socket.id)

    socket.on('create-room', (roomName) => {
      socket.join(roomName)
      socket.roomByRooms = roomName
      RoomService.createRoom(socket, roomName, listRooms)
    })

    socket.on('enter-kitchen-success', (accessToken, newOrder) => {
      let cashierTs = new Date().getTime()

      newOrder.ts = cashierTs

      _io.sockets.in(socket.roomByRooms).emit('enter-kitchen-success', newOrder)

      KitchenService.getAllItems(socket, accessToken)
    })

    socket.on(
      'change-cooking-to-waiter-success-one-product-of-product',
      (tableObj, accessToken) => {
        let cashierTs = new Date().getTime()
        tableObj.ts = cashierTs

        _io.sockets
          .in(socket.roomByRooms)
          .emit(
            'change-cooking-to-waiter-success-one-product-of-product',
            tableObj
          )

        KitchenService.getAllItems(socket, accessToken)
      }
    )

    socket.on(
      'change-cooking-to-waiter-success-all-product-of-product',
      (accessToken) => {
        let cashierTs = new Date().getTime()

        const obj = {
          ts: cashierTs,
        }

        _io.sockets
          .in(socket.roomByRooms)
          .emit('change-cooking-to-waiter-success-all-product-of-product', obj)

        KitchenService.getAllItems(socket, accessToken)
      }
    )

    socket.on(
      'change-cooking-to-waiter-success-all-product-of-table',
      (tableObj, accessToken) => {
        let cashierTs = new Date().getTime()

        tableObj.ts = cashierTs

        _io.sockets
          .in(socket.roomByRooms)
          .emit(
            'change-cooking-to-waiter-success-all-product-of-table',
            tableObj
          )

        KitchenService.getAllItems(socket, accessToken)
      }
    )

    socket.on('change-waiter-to-delivery-success-of-product', () => {
      let cashierTs = new Date().getTime()

      const obj = {
        ts: cashierTs,
      }

      _io.sockets
        .in(socket.roomByRooms)
        .emit('change-waiter-to-delivery-success-of-product', obj)
    })

    socket.on('change-cooking-to-waiter-success', (obj) => {
      let cashierTs = new Date().getTime()
      obj.ts = cashierTs

      _io.sockets
        .in(socket.roomByRooms)
        .emit('change-cooking-to-waiter-success', obj)
    })

    socket.on('change-waiter-to-delivery-success', (obj) => {
      let cashierTs = new Date().getTime()
      obj.ts = cashierTs

      _io.sockets
        .in(socket.roomByRooms)
        .emit('change-waiter-to-delivery-success', obj)
    })

    socket.on(
      'change-cooking-to-stock-out-success',
      (tableObj, accessToken) => {
        let cashierTs = new Date().getTime()
        tableObj.ts = cashierTs

        _io.sockets
          .in(socket.roomByRooms)
          .emit('change-cooking-to-stock-out-success', tableObj)

        KitchenService.getAllItems(socket, accessToken)
      }
    )

    socket.on('change-cooking-to-waiter-success', (tableObj, accessToken) => {
      let cashierTs = new Date().getTime()
      tableObj.ts = cashierTs

      _io.sockets
        .in(socket.roomByRooms)
        .emit('change-cooking-to-waiter-success', tableObj)

      KitchenService.getAllItems(socket, accessToken)
    })

    socket.on('change-waiter-to-delivery-success-of-kitchen', (accessToken) => {
      KitchenService.getAllItems(socket, accessToken)
    })

    socket.on(
      'change-waiter-to-stockout-success-of-kitchen',
      (accessToken, tableObj) => {
        KitchenService.getAllItems(socket, accessToken)

        let cashierTs = new Date().getTime()
        tableObj.ts = cashierTs

        _io.sockets
          .in(socket.roomByRooms)
          .emit('change-waiter-to-stock-out-success', tableObj)
      }
    )

    socket.on('enter-kitchen-success-to-kitchen', (accessToken) => {
      KitchenService.getAllItems(socket, accessToken)
    })
  }
}
module.exports = new SocketService()
