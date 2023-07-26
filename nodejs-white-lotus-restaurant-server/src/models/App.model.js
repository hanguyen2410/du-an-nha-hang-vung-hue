const SERVER_API = process.env.SERVER_API
const BASE_ORDER_API = SERVER_API + '/orders'
const BASE_ORDER_ITEM_API = SERVER_API + '/order-items'

class App {
  static GET_ALL_KITCHEN_DATA = `${BASE_ORDER_ITEM_API}/kitchen/get-all`

  static ADD_ITEM_KITCHEN = `${BASE_ORDER_API}`
}

module.exports = App
