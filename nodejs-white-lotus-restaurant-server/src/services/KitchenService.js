const axios = require('axios')

const App = require('../models/App.model')

const getAllItems = async (socket, accessToken) => {
  try {
    axios.defaults.headers.common.Authorization = accessToken

    const getKitchenData = await axios.get(App.GET_ALL_KITCHEN_DATA)

    let kitchenTs = new Date().getTime()

    let kitchenObj = {
      clientId: socket.id,
      data: getKitchenData.data,
      ts: kitchenTs,
    }

    _io.sockets.in(socket.roomByRooms).emit('get-all-data-kitchen', kitchenObj)
  } catch (error) {
    socket.emit('order-product-error', 'Lỗi hệ thống, không tải được dữ liệu!')
  }
}

let KitchenService = {
  getAllItems,
}

module.exports = KitchenService
