const axios = require('axios')

const App = require('../models/App.model')

const addItem = async (socket, accessToken, orderRequest) => {
  try {
    axios.defaults.headers.common.Authorization = accessToken
    const result = await axios.post(App.ADD_ITEM_KITCHEN, orderRequest)

    let cashierTs = new Date().getTime()

    let cashierObj = {
      clientId: socket.id,
      data: result.data,
      ts: cashierTs,
    }

    _io.sockets.to(socket.id).emit('order-product-success', cashierObj)

    const updateTable = {
      id: result.data.orderWithOrderItemResDTO.table.id,
      name: result.data.orderWithOrderItemResDTO.table.name,
      ts: cashierTs,
    }

    _io.sockets
      .in(socket.roomByRooms)
      .emit('enter-kitchen-success', updateTable)

    // const getKitchenData = await axios.get(App.GET_ALL_KITCHEN_DATA)

    // let kitchenTs = new Date().getTime()

    // let kitchenObj = {
    //   clientId: socket.id,
    //   data: getKitchenData.data,
    //   tableName: result.data.orderWithOrderItemResDTO.table.name,
    //   ts: kitchenTs,
    // }

    // _io.sockets
    //   .in(socket.roomByRooms)
    //   .emit('cashier-order-product-success', kitchenObj)
  } catch (error) {
    socket.emit('order-product-error', 'Lỗi hệ thống, không gọi được món!')
  }
}

let OrderItemService = {
  addItem,
}

module.exports = OrderItemService
