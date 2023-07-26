import React, { useState, useEffect } from 'react'
import { Helmet } from 'react-helmet-async'
import { useDispatch, useSelector } from 'react-redux'
import { toast } from 'react-toastify'
import { LoadingButton } from '@mui/lab'

import Iconify from 'components/Iconify'

import Swal from 'sweetalert2'
import 'layouts/sweetalert.css'

import { Grid, Container, Typography, Stack, Box } from '@mui/material'

import { TABLE_STATUS } from 'constants/global'
import { EnumStatus } from 'constants/EnumStatus'

import {
  updateCurrentTable,
  setCurrentTableOrderId,
  emptyCurrentTable,
  setCurrentTableAddItemStatus,
  setDataLoading,
  updateChangeTableStatus,
  updateChangeTableCombineStatus,
  updateTableStatus,
  fetchTablesAndCategoriesData,
  fetchProductsData,
} from 'features/Home/restaurantSlice'

import {
  setCurrentOrderItems,
  resetOrderItems,
  getOrderItemsByTableId,
  setTotalAmount,
  setCurrentOrderItem,
  addItemKitchen,
  resetCurrentOrderItems,
  setUpdateOrderItems,
} from 'features/Home/orderItemSlice'

import 'layouts/sweetalert.css'

import Loading from './loading/Loading'

import authService from 'services/authService'
import cashierService from 'services/cashierService'

import Helper from 'utils/Helper'

import AppOrderDetail from './components/AppOrderDetail/AppOrderDetail'
import MenuBar from './components/MenuBar'
import ModalOrder from './components/ModalOrder'
import Footer from './components/Footer'
import TabTableAndMenu from './components/TabTableAndMenu'
import BodyTableAndMenu from './components/BodyTableAndMenu'
import TabOrderItems from './components/TabOrderItems'
import StaffInfo from './components/StaffInfo'
import BodyOrderItemsEmpty from './components/BodyOrderItemsEmpty'
import ModalBill from './components/ModalBill'

import { OrderItemStatus } from 'constants/OrderItemStatus'
import { initTotalAmount, resetPaymentData } from './paymentSlice'
import { updateTempBillData } from './billSlice'
// ----------------------------------------------------------------------

let ts
let tableDataObj = {}
const socket = authService.connectServer()

const accessToken = localStorage.getItem('wl_accessToken')

export default function Home() {
  const dispatch = useDispatch()

  const authData = useSelector((state) => state.authData.data)
  const fullName = authData.fullName

  const baseData = useSelector((state) => state.baseData.data)

  const resProducts = baseData.products
  const currentTable = baseData.currentTable
  const loadingGlobal = baseData.loading

  // const [currentTableObj, setCurrentTableObj] = useState(baseData.currentTable)

  const orderItemData = useSelector((state) => state.orderItemData.data)
  const currentOrderItemsdata = orderItemData.currentOrderItemsdata
  const updateOrderItemsdata = orderItemData.updateOrderItemsdata
  const totalAmount = orderItemData.totalAmount
  // const currentOrderItem = orderItemData.currentOrderItem

  // console.log(currentOrderItemsdata)
  // console.log(updateOrderItemsdata)

  // let orderItemsData = []

  // if (
  //   currentOrderItemsdata !== undefined &&
  //   currentOrderItemsdata !== [] &&
  //   updateOrderItemsdata !== undefined &&
  //   updateOrderItemsdata !== []
  // ) {
  //   orderItemsData = currentOrderItemsdata.concat(updateOrderItemsdata)
  // }

  // let staffName

  // const [state, setState] = useState({
  //   erroMessage: '',
  // })

  const [openModalOrder, setOpenModalOrder] = useState(false)
  const [checkEditModal, setCheckEditModal] = useState(false)
  const [openModalBill, setOpenModalBill] = useState(false)
  const [loadingKitchen, setLoadingKitchen] = useState(false)
  const [loadingPay, setLoadingPay] = useState(false)
  // const [checkTabBill, setCheckTabBill] = useState(false)

  const [checkTab, setCheckTab] = useState({
    table: true,
    menu: false,
    billHistory: false,
  })

  // Khởi tạo dữ liệu
  useEffect(() => {
    const getData = () => {
      dispatch(setDataLoading(true))

      dispatch(fetchTablesAndCategoriesData())
        .unwrap()
        .then(() => {
          dispatch(fetchProductsData())
        })
        .catch(() => {
          toast.warning('Vui lòng kiểm tra kết nối mạng !')
        })
        .finally(() => {
          dispatch(setDataLoading(false))
        })
    }

    getData()
  }, [])

  // Tìm sp theo id
  const findProductById = (id) => {
    return resProducts.find((item) => item.productId === id)
  }

  // Mở ModalOrder
  const handleOpenModalOrder = (productId) => {
    const product = Object.assign({}, findProductById(productId))

    if (Object.keys(product).length) {
      product.quantity = 1
      product.amount = product.price
      product.note = ''
      product.status = OrderItemStatus.NEW.status

      const setCurrentOrderItemAction = setCurrentOrderItem(product)
      dispatch(setCurrentOrderItemAction)

      setOpenModalOrder(true)
    } else {
      toast.error('Không tìm thấy sản phẩm tương ứng')
    }
  }

  // Ẩn ModalOrder
  const handleCloseMorderOrder = () => {
    setOpenModalOrder(false)
  }

  // Chuyển qua menu đặt món
  // const showMenu = () => {
  //   currentTable
  //     ? setCheckTab({
  //         ...checkTab,
  //         table: false,
  //         menu: true,
  //         billHistory: false,
  //       })
  //     : toast.warning('Vui lòng chọn bàn !')
  // }

  // Đóng bàn
  const closeTable = () => {
    tableDataObj = {}
    dispatch(emptyCurrentTable())
    dispatch(resetOrderItems())
    setCheckTab({
      ...checkTab,
      table: true,
      menu: false,
      billHistory: false,
    })
  }

  // Hiện ModalOrder để update
  const handleOpenEditModal = (orderItem, index) => {
    dispatch(setCurrentOrderItem(orderItem))
    setCheckEditModal(true)
    setOpenModalOrder(true)
  }

  //Chọn bàn
  const handleSelectTable = async (selectedTable) => {
    if (
      updateOrderItemsdata.length > 0 &&
      selectedTable.id !== currentTable.id
    ) {
      Swal.fire({
        title: `${currentTable.name} đang có những món chưa nhập bếp`,
        text: 'Những món chưa nhập bếp sẽ bị xóa khỏi danh sách chờ',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Đồng ý',
        cancelButtonText: 'Hủy',
      }).then((result) => {
        if (result.isConfirmed) {
          const resetOrderItemAction = resetOrderItems()
          dispatch(resetOrderItemAction)

          getOrderByTable(selectedTable)
        }
      })
    } else {
      getOrderByTable(selectedTable)
    }
  }

  // Xem bàn
  const getOrderByTable = async (selectedTable) => {
    dispatch(setDataLoading(true))

    if (Object.keys(selectedTable).length) {
      try {
        tableDataObj = selectedTable

        const updateCurrentTableAction = updateCurrentTable(selectedTable)
        dispatch(updateCurrentTableAction)

        const orderTableRes = await cashierService.getOrderByTable(
          selectedTable.id
        )

        if (orderTableRes.status === 204) {
          const resetCurrentOrderItemsAction = resetCurrentOrderItems()
          dispatch(resetCurrentOrderItemsAction)

          const setTableAddItemStatusAction = setCurrentTableAddItemStatus(
            TABLE_STATUS.NEW
          )
          dispatch(setTableAddItemStatusAction)
        } else {
          const setTableAddItemStatusAction = setCurrentTableAddItemStatus(
            TABLE_STATUS.UPDATE
          )
          dispatch(setTableAddItemStatusAction)

          const setCurrentTableOrderIdAction = setCurrentTableOrderId(
            orderTableRes.data.orderId
          )
          dispatch(setCurrentTableOrderIdAction)

          const action = setCurrentOrderItems(orderTableRes.data.orderItems)
          dispatch(action)
        }

        dispatch(setDataLoading(false))

        setCheckTab({
          ...checkTab,
          table: false,
          menu: true,
          billHistory: false,
        })
      } catch (error) {
        const resetCurrentOrderItemsAction = resetCurrentOrderItems()
        dispatch(resetCurrentOrderItemsAction)

        const setTableAddItemStatusAction = setCurrentTableAddItemStatus(
          TABLE_STATUS.NEW
        )
        dispatch(setTableAddItemStatusAction)

        dispatch(setDataLoading(false))

        toast.error(`Không thể lấy dữ liệu của ${selectedTable.name}`)
      }
    } else {
      toast.error(`Thông tin ${selectedTable.name} không hợp lệ`)
      dispatch(setDataLoading(false))
    }

    const setTotalAmountAction = setTotalAmount()
    dispatch(setTotalAmountAction)
  }

  // Mở ModalBill
  const handleOpenBill = async () => {
    if (currentTable === null) {
      toast.error('Vui lòng chọn bàn!')
      return
    }
    if (currentOrderItemsdata.length === 0) {
      toast.error('Vui lòng chọn món trước!')
      return
    }
    if (updateOrderItemsdata.length !== 0) {
      toast.warning('Vui lòng nhập bếp các món ăn vừa thêm!')
      return
    }

    for (let i = 0; i < currentOrderItemsdata.length; i++) {
      if (currentOrderItemsdata[i].status === OrderItemStatus.COOKING.status) {
        toast.warning('Bàn này đang có món đang nấu')
        return
      }

      if (currentOrderItemsdata[i].status === OrderItemStatus.WAITER.status) {
        toast.warning('Bàn này đang có món chờ phục vụ')
        return
      }

      if (
        currentOrderItemsdata[i].status === OrderItemStatus.STOCK_OUT.status
      ) {
        toast.warning('Vui lòng xóa những món đã hết hàng')
        return
      }
    }

    const setTotalAmountAction = initTotalAmount(orderItemData.totalAmount)
    dispatch(setTotalAmountAction)

    let billTempData = []

    currentOrderItemsdata.forEach((orderItem) => {
      if (billTempData.length === 0) {
        let obj = Object.assign({}, orderItem)
        billTempData.push(obj)
      } else {
        let exist = false
        let itemIndex = -1

        billTempData.forEach((billItem, billIndex) => {
          if (orderItem.productId === billItem.productId) {
            exist = true
            itemIndex = billIndex
          }
        })

        if (exist) {
          const newQuantity =
            billTempData[itemIndex].quantity + orderItem.quantity
          billTempData[itemIndex].quantity = newQuantity
          billTempData[itemIndex].amount = newQuantity * orderItem.price
        } else {
          let obj = Object.assign({}, orderItem)
          billTempData.push(obj)
        }

        exist = false
        itemIndex = -1
      }
    })

    const updateTempBillDataAction = updateTempBillData(billTempData)
    dispatch(updateTempBillDataAction)

    setOpenModalBill(true)
  }

  // Đóng ModalBill
  const handleCloseBill = () => {
    setOpenModalBill(false)

    const action = resetPaymentData()
    dispatch(action)
  }

  //Nhập bếp
  const handleEnterTheKitchen = () => {
    let obj = {
      tableId: currentTable.id,
      status: currentTable.addItemStatus,
      items: updateOrderItemsdata,
    }

    setLoadingKitchen(true)

    dispatch(addItemKitchen(obj))
      .unwrap()
      .then((res) => {
        const newOrder = res.orderWithOrderItemResDTO
        socket.emit('enter-kitchen-success', accessToken, newOrder)

        const setCurrentOrderItemsAction = setCurrentOrderItems(
          newOrder.orderItems
        )
        dispatch(setCurrentOrderItemsAction)

        const setUpdateOrderItemsAction = setUpdateOrderItems([])
        dispatch(setUpdateOrderItemsAction)

        const updateTableStatusAction = updateTableStatus(newOrder)
        dispatch(updateTableStatusAction)

        if (res.tableCombine) {
          const updateChangeTableCombineStatusAction =
            updateChangeTableCombineStatus(newOrder)
          dispatch(updateChangeTableCombineStatusAction)
        }

        const setCurrentTableAddItemStatusAction = setCurrentTableAddItemStatus(
          TABLE_STATUS.UPDATE
        )
        dispatch(setCurrentTableAddItemStatusAction)

        const setCurrentTableOrderIdAction = setCurrentTableOrderId(
          newOrder.orderId
        )
        dispatch(setCurrentTableOrderIdAction)

        toast.success(`Nhập bếp thành công`)
      })
      .catch(() => {
        toast.error(`Nhập bếp không thành công`)
      })
      .finally(() => {
        setLoadingKitchen(false)
      })
  }

  socket.on('enter-kitchen-success', (res) => {
    if (ts !== res.ts) {
      ts = res.ts
      const tableId = res.table.id

      if (tableId === tableDataObj.id) {
        const setCurrentOrderItemsAction = setCurrentOrderItems(res.orderItems)
        dispatch(setCurrentOrderItemsAction)
      }

      const updateTable = {
        tableId,
        status: EnumStatus.BUSY.status,
        statusValue: EnumStatus.BUSY.statusValue,
      }
      const updateChangeTableStatusAction = updateChangeTableStatus(updateTable)
      dispatch(updateChangeTableStatusAction)
    }
  })

  socket.on(
    'change-cooking-to-waiter-success-one-product-of-product',
    (res) => {
      if (ts !== res.ts) {
        ts = res.ts
        const tableId = res.tableId

        if (tableId === tableDataObj.id) {
          getOrderItemsByTable(tableId)
        }
      }
    }
  )

  socket.on(
    'change-cooking-to-waiter-success-all-product-of-product',
    (res) => {
      if (ts !== res.ts) {
        ts = res.ts
        if (Object.keys(tableDataObj).length) {
          getOrderItemsByTable(tableDataObj.id)
        }
      }
    }
  )

  socket.on('change-cooking-to-waiter-success-all-product-of-table', (res) => {
    if (ts !== res.ts) {
      ts = res.ts

      if (Object.keys(tableDataObj).length) {
        getOrderItemsByTable(tableDataObj.id)
      }
    }
  })

  socket.on('change-waiter-to-delivery-success-of-product', (res) => {
    if (ts !== res.ts) {
      ts = res.ts

      if (Object.keys(tableDataObj).length) {
        getOrderItemsByTable(tableDataObj.id)
      }
    }
  })

  socket.on('change-cooking-to-waiter-success', (res) => {
    if (ts !== res.ts) {
      ts = res.ts

      if (res.tableId === tableDataObj.id) {
        getOrderItemsByTable(res.tableId)
      }
    }
  })

  socket.on('change-waiter-to-delivery-success', (res) => {
    if (ts !== res.ts) {
      ts = res.ts

      if (res.tableId === tableDataObj.id) {
        getOrderItemsByTable(res.tableId)
      }
    }
  })

  socket.on('change-cooking-to-stock-out-success', (res) => {
    if (ts !== res.ts) {
      ts = res.ts

      toast.warning(`${res.tableName} có món hết hàng`)

      if (res.tableId === tableDataObj.id) {
        getOrderItemsByTable(res.tableId)
      }
    }
  })

  socket.on('change-waiter-to-stock-out-success', (res) => {
    if (ts !== res.ts) {
      ts = res.ts

      toast.warning(`${res.tableName} có món hết hàng`)

      if (res.tableId === tableDataObj.id) {
        getOrderItemsByTable(res.tableId)
      }
    }
  })

  const getOrderItemsByTable = (tableId) => {
    const getOrderItemsByTableIdAction = getOrderItemsByTableId({ tableId })
    dispatch(getOrderItemsByTableIdAction)
  }

  return (
    <>
      <Helmet>
        {' '}
        <title> White Lotus | Thu ngân</title>
      </Helmet>

      {loadingGlobal && <Loading />}

      <ModalBill
        openModalBill={openModalBill}
        handleCloseBill={handleCloseBill}
        closeTable={closeTable}
      />

      <ModalOrder
        openModalOrder={openModalOrder}
        handleCloseMorderOrder={handleCloseMorderOrder}
        checkEditModal={checkEditModal}
      />

      <Container
        maxWidth="100%"
        sx={{
          backgroundColor: '#7266ba',
          height: '100%',
          paddingBottom: '35px',
        }}
      >
        <Grid container spacing={2}>
          <Grid item xs={7} md={7} lg={7}>
            <TabTableAndMenu checkTab={checkTab} setCheckTab={setCheckTab} />

            <BodyTableAndMenu
              checkTab={checkTab}
              setCheckTab={setCheckTab}
              handleOpenModalOrder={handleOpenModalOrder}
              handleSelectTable={handleSelectTable}
            />
          </Grid>

          <Grid item xs={5} md={5} lg={5}>
            <TabOrderItems closeTable={closeTable} />
            <StaffInfo fullName={fullName} />
            <MenuBar />

            <Box
              style={{
                // height: 430,
                height: '70vh',
                overflow: 'hidden',
                // overflowY: 'scroll',
                padding: '10px',
                backgroundColor: '#F9FAFB',
                borderRadius: '12px 12px 0 0',
              }}
              className="wrapperBoard"
            >
              <Box
                style={{
                  height: '100%',
                  // height: '87vh',
                  overflow: 'auto',
                  overflowY: 'scroll',
                  // padding: 4,
                  backgroundColor: '#0008100a',
                  // borderRadius: '12px',
                }}
                className="wrapperBoard"
              >
                {currentOrderItemsdata.length || updateOrderItemsdata.length ? (
                  <AppOrderDetail
                    handleOpenEditModal={handleOpenEditModal}
                    handleCloseBill={handleCloseBill}
                    closeTable={closeTable}
                  />
                ) : (
                  <BodyOrderItemsEmpty />
                )}
              </Box>
            </Box>

            <Box
              style={{
                height: '20vh',
                padding: '10px',
                backgroundColor: '#5b509a',
                borderRadius: '0 0 12px 12px',
              }}
              className="wrapperBoard"
            >
              <Stack
                direction="row"
                alignItems="center"
                justifyContent="space-between"
                sx={{
                  padding: '2px 10px',
                  m: '5px 2px',
                  height: '30%',
                }}
              >
                {/* <Stack>
                <Typography
                  variant="body2"
                  sx={{
                    color: 'white',
                    fontWeight: 600,
                    fontSize: '16px',
                    fontFamily: 'sans-serif',
                  }}
                >
                  Giảm Giá
                </Typography>
                <FilledInput
                  variant="body2"
                  sx={{
                    color: 'white',
                    fontWeight: 600,
                    fontSize: '10px',
                  }}
                >
                </FilledInput>
                </Stack> */}
                <Typography
                  variant="body2"
                  sx={{
                    color: 'white',
                    fontWeight: 600,
                    fontSize: '16px',
                    fontFamily: 'sans-serif',
                  }}
                >
                  TỔNG TIỀN
                </Typography>
                <Typography
                  variant="body2"
                  sx={{
                    color: '#52e96f',
                    fontWeight: 700,
                    fontSize: '18px',
                    fontFamily: 'sans-serif',
                  }}
                >
                  {Helper.formatCurrencyToVND(totalAmount)}
                </Typography>
              </Stack>
              <Stack
                direction="row"
                alignItems="center"
                justifyContent="space-between"
                sx={{
                  padding: '2.5% 10px',
                  m: '5px 2px',
                  height: '70%',
                }}
              >
                <LoadingButton
                  variant="contained"
                  sx={{
                    color: 'white',
                    backgroundColor: '#0066CC',
                    width: '48%',
                    fontSize: '20px',
                    fontFamily: 'serif',
                    borderRadius: '12px',
                    '&:hover': {
                      backgroundColor: 'white',
                      color: '#d0181b',
                    },
                    cursor: 'pointer',
                    // height: '100%',
                    height: '60px',
                  }}
                  loading={loadingKitchen}
                  onClick={handleEnterTheKitchen}
                  disabled={updateOrderItemsdata.length === 0}
                >
                  <Iconify
                    icon={'ep:dish'}
                    width="25px"
                    height={50}
                    sx={{
                      mr: 1,
                    }}
                  />
                  Nhập bếp
                </LoadingButton>
                <LoadingButton
                  variant="contained"
                  sx={{
                    color: 'white',
                    backgroundColor: '#28B44F',
                    width: '48%',
                    fontSize: '20px',
                    fontFamily: 'serif',
                    borderRadius: '12px',
                    '&:hover': {
                      backgroundColor: 'white',
                      color: '#d0181b',
                    },
                    // height: '100%',
                    height: '60px',
                  }}
                  loading={loadingPay}
                  onClick={handleOpenBill}
                  disabled={currentOrderItemsdata.length === 0}
                >
                  <Iconify
                    icon={'ri:money-dollar-circle-line'}
                    width="25px"
                    height={50}
                    sx={{
                      mr: 1,
                    }}
                  />
                  Thanh toán
                </LoadingButton>
              </Stack>
            </Box>
          </Grid>
        </Grid>
      </Container>
      <Footer />
    </>
  )
}
