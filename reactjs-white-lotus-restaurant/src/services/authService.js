import axiosClient from './axiosClient'
import { AUTH_LOGIN_URL, SOCKET_SERVER } from 'constants/global'
import socketIOClient from 'socket.io-client'

let socket

const authService = {
  login: (acount) => {
    return axiosClient.post(AUTH_LOGIN_URL, acount)
  },
  connectServer: () => {
    socket = socketIOClient(SOCKET_SERVER)
    socket.emit('create-room', 'White-Lotus')
    return socket
  },
}

export default authService
