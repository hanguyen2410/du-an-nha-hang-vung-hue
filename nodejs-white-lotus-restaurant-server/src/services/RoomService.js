let createRoom = (socket, roomName, listRooms) => {
  socket.roomByRooms = roomName

  if (listRooms.indexOf(roomName) >= 0) {
  } else {
    listRooms.push(roomName)
  }
}

let RoomService = {
  createRoom,
}

module.exports = RoomService
