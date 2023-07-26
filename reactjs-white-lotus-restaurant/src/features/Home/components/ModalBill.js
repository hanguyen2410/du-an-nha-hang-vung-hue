import React, { useState } from 'react'
import {
  Grid,
  Typography,
  Paper,
  Stack,
  Divider,
  TextField,
  Backdrop,
  Box,
  Modal,
  styled,
  Input,
} from '@mui/material'

import Iconify from 'components/Iconify'

import Helper from 'utils/Helper'

import Table from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell, { tableCellClasses } from '@mui/material/TableCell'
import TableContainer from '@mui/material/TableContainer'
import TableHead from '@mui/material/TableHead'
import TableRow from '@mui/material/TableRow'

import { useDispatch, useSelector } from 'react-redux'

import Images from 'constants/images'

import { PropTypes } from 'prop-types'

import Slide from '@mui/material/Slide'

import { NumericFormat } from 'react-number-format'

import { LoadingButton } from '@mui/lab'

import { toast } from 'react-toastify'

import { OrderItemStatus } from 'constants/OrderItemStatus'

import { updateChangeTableStatus } from 'features/Home/restaurantSlice'

import {
  payBill,
  setChangeMoney,
  setDiscount,
  setFees,
  setReceivedCashMoney,
  setReceivedTransferMoney,
  updateTotalAmount,
} from 'features/Home/paymentSlice'
import { EnumStatus } from 'constants/EnumStatus'

// ----------------------------------------------------------------------

const style = {
  position: 'absolute',
  top: '0',
  right: 0,
  transform: 'translate(-50%, -50%)',
  width: '65%',
  height: '100vh',
  bgcolor: 'background.paper',
  // border: '2px solid #d8d8df',
  borderRadius: '12px',
  boxShadow: 24,
  p: 3,
}

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: theme.palette.common.black,
    color: theme.palette.common.white,
    lineHeight: '7px',
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
    lineHeight: '5px',
    paddingRight: 0,
    paddingLeft: 0,
  },
}))

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  '&:nth-of-type(odd)': {
    backgroundColor: theme.palette.action.hover,
  },
  // hide last border
  '&:last-child td, &:last-child th': {
    border: 0,
  },
}))

ModalBill.prototype = {
  openBill: PropTypes.bool,
}

// ----------------------------------------------------------------------

export default function ModalBill({
  openModalBill,
  handleCloseBill,
  closeTable,
}) {
  const dispatch = useDispatch()

  const authData = useSelector((state) => state.authData.data)
  const fullName = authData.fullName

  const paymentData = useSelector((state) => state.paymentData.data)
  const billData = useSelector((state) => state.billData.data)
  const tempBillData = billData.tempBillData

  const baseData = useSelector((state) => state.baseData.data)
  const currentTable = baseData.currentTable

  const orderItemData = useSelector((state) => state.orderItemData.data)
  const currentOrderItemsdata = orderItemData.currentOrderItemsdata

  let currentOrderItemsDONE = []
  let totalAmountBill = 0

  currentOrderItemsdata.forEach((item) => {
    if (item.status === OrderItemStatus.DELIVERY.status) {
      totalAmountBill += +item.quantity * item.price
      currentOrderItemsDONE.push(item)
    }
  })

  const [state, setState] = useState({
    loading: false,
  })

  const [loadingPrintBillTemp, setLoadingPrintBillTemp] = useState(false)
  const [loadingPayBill, setLoadingPayBill] = useState(false)

  const { loading } = state
  const handleChangeFees = (e) => {
    const fees = e.target.value.length === 0 ? 0 : +e.target.value

    if (fees >= 0 && fees <= 100) {
      const receivedCashMoney = paymentData.receivedCashMoney
      const receivedTransferMoney = paymentData.receivedTransferMoney

      const obj = {
        fees,
        totalPrice: orderItemData.totalAmount,
        receivedCashMoney,
        receivedTransferMoney,
      }

      const setFeesAction = setFees(obj)
      dispatch(setFeesAction)

      const updateTotalAmountAction = updateTotalAmount(obj)
      dispatch(updateTotalAmountAction)

      const setChangeMoneyAction = setChangeMoney(obj)
      dispatch(setChangeMoneyAction)
    }
  }

  const handleChangeDiscount = (e) => {
    const discount = e.target.value.length === 0 ? 0 : +e.target.value

    if (discount >= 0 && discount <= 100) {
      const receivedCashMoney = paymentData.receivedCashMoney
      const receivedTransferMoney = paymentData.receivedTransferMoney

      const obj = {
        discount,
        totalPrice: orderItemData.totalAmount,
        receivedCashMoney,
        receivedTransferMoney,
      }

      const setDiscountAction = setDiscount(obj)
      dispatch(setDiscountAction)

      const updateTotalAmountAction = updateTotalAmount(obj)
      dispatch(updateTotalAmountAction)

      const setChangeMoneyAction = setChangeMoney(obj)
      dispatch(setChangeMoneyAction)
    }
  }

  const handleCashMoney = (e) => {
    const receivedCashMoney =
      e.target.value.length === 0 ? 0 : +e.target.value.replaceAll(',', '')
    const receivedTransferMoney = paymentData.receivedTransferMoney
    const obj = {
      totalPrice: orderItemData.totalAmount,
      receivedCashMoney,
      receivedTransferMoney,
    }

    const setReceivedCashMoneyAction = setReceivedCashMoney(obj)
    dispatch(setReceivedCashMoneyAction)

    const setReceivedTransferMoneyAction = setReceivedTransferMoney(obj)
    dispatch(setReceivedTransferMoneyAction)

    const updateTotalAmountAction = updateTotalAmount(obj)
    dispatch(updateTotalAmountAction)

    const setChangeMoneyAction = setChangeMoney(obj)
    dispatch(setChangeMoneyAction)
  }

  const handleTransferMoney = (e) => {
    const receivedTransferMoney =
      e.target.value.length === 0 ? 0 : +e.target.value.replaceAll(',', '')
    const receivedCashMoney = paymentData.receivedCashMoney

    const obj = {
      totalPrice: orderItemData.totalAmount,
      receivedTransferMoney,
      receivedCashMoney,
    }

    const setReceivedTransferMoneyAction = setReceivedTransferMoney(obj)
    dispatch(setReceivedTransferMoneyAction)

    const setReceivedCashMoneyAction = setReceivedCashMoney(obj)
    dispatch(setReceivedCashMoneyAction)

    const updateTotalAmountAction = updateTotalAmount(obj)
    dispatch(updateTotalAmountAction)

    const setChangeMoneyAction = setChangeMoney(obj)
    dispatch(setChangeMoneyAction)
  }

  //In bill tạm
  const handlePayTemp = async () => {
    try {
      setLoadingPrintBillTemp(true)

      // let billTempData = []

      // currentOrderItemsdata.forEach((orderItem) => {
      //   if (billTempData.length === 0) {
      //     let obj = Object.assign({}, orderItem)
      //     billTempData.push(obj)
      //   } else {
      //     let exist = false
      //     let itemIndex = -1

      //     billTempData.forEach((billItem, billIndex) => {
      //       if (orderItem.productId === billItem.productId) {
      //         exist = true
      //         itemIndex = billIndex
      //       }
      //     })

      //     if (exist) {
      //       const newQuantity =
      //         billTempData[itemIndex].quantity + orderItem.quantity
      //       billTempData[itemIndex].quantity = newQuantity
      //       billTempData[itemIndex].amount = newQuantity * orderItem.price
      //     } else {
      //       let obj = Object.assign({}, orderItem)
      //       billTempData.push(obj)
      //     }

      //     exist = false
      //     itemIndex = -1
      //   }
      // })

      // const updateTempBillDataAction = updateTempBillData(billTempData)
      // dispatch(updateTempBillDataAction)

      setTimeout(() => {
        console.log(document.getElementById('billPapperTemp'))
        const content = document.getElementById('billPapperTempContent')
        const pri = document.getElementById('billPapperTemp').contentWindow
        pri.document.open()
        pri.document.write(content.innerHTML)
        pri.document.close()
        pri.focus()
        pri.print()
        // handlePayMoneyToClose(currentTable.id);
        // moneyReceived = 0

        setLoadingPrintBillTemp(false)
      }, 500)
    } catch (error) {
      toast.error('Lỗi hệ thống, vui lòng kiểm tra lại dữ liệu!')
    }
  }

  //Thanh toán bill
  const handlePay = () => {
    if (
      paymentData.receivedTransferMoney + paymentData.receivedCashMoney ===
      0
    ) {
      toast.error('Chưa nhập số tiền!')
      return
    }

    if (
      paymentData.receivedTransferMoney + paymentData.receivedCashMoney <
      paymentData.totalAmount
    ) {
      toast.error('Số tiền không đủ!')
      return
    }

    if (paymentData.receivedTransferMoney > paymentData.totalAmount) {
      toast.error('Số tiền chuyển khoản không được vượt quá tổng tiền.')
      return
    }

    if (paymentData.receivedCashMoney < 0) {
      toast.error('Số tiền khách đưa không được âm.')
      return
    }

    if (paymentData.receivedTransferMoney < 0) {
      toast.error('Số tiền khách chuyển khoản không được âm.')
      return
    }

    let obj = {
      orderId: currentTable.orderId,
      chargePercent: paymentData.fees,
      chargeMoney: paymentData.feesMoney,
      discountPercent: paymentData.discount,
      discountMoney: paymentData.discountMoney,
      totalAmount: paymentData.totalAmount,
      receivedTransferMoney: paymentData.receivedTransferMoney,
      receivedCashMoney:
        paymentData.receivedCashMoney - paymentData.changeMoney,
    }

    setLoadingPayBill(true)

    setTimeout(() => {
      const content = document.getElementById('billPapperContent')
      const pri = document.getElementById('billPapper').contentWindow
      pri.document.open()
      pri.document.write(content.innerHTML)
      pri.document.close()
      pri.focus()
      pri.print()
      // handlePayMoneyToClose(currentTable.id);
      // moneyReceived = 0

      setLoadingPrintBillTemp(false)
    }, 0)

    dispatch(payBill(obj))
      .unwrap()
      .then(() => {
        const objTable = {
          tableId: currentTable.id,
          status: EnumStatus.EMPTY.status,
          statusValue: EnumStatus.EMPTY.statusValue,
        }

        const updateTableStatusAction = updateChangeTableStatus(objTable)
        dispatch(updateTableStatusAction)

        closeTable()
        handleCloseBill()

        setLoadingPayBill(false)
        toast.success('Đã thanh toán ' + currentTable.name)
      })
      .catch(() => {
        toast.error(`Thanh toán thất bại, vui lòng kiểm tra dữ liệu`)
        setLoadingPayBill(false)
      })
  }

  return (
    <>
      <Modal
        aria-labelledby="transition-modal-title"
        aria-describedby="transition-modal-description"
        open={openModalBill}
        onClose={handleCloseBill}
        closeAfterTransition
        BackdropComponent={Backdrop}
        BackdropProps={{
          timeout: 500,
        }}
        disableAutoFocus
      >
        <Slide direction="left" in={openModalBill}>
          <Box sx={style}>
            <Iconify
              icon={'material-symbols:close'}
              position="absolute"
              top="10px"
              right="10px"
              bgcolor="white"
              color="#5A5A72"
              borderRadius="50%"
              width={35}
              padding={0.6}
              cursor="pointer"
              sx={{
                '&:hover': {
                  color: '#131318',
                  bgcolor: '#EBEBEF',
                  border: '1px solid #B9B9c6',
                  boxShadow: 'rgba(0, 0, 0, 0.2) 0px 2px 12px 0px',
                },
              }}
              onClick={handleCloseBill}
            />
            <Typography
              id="transition-modal-title"
              variant="h6"
              component="h2"
              sx={{
                color: 'black',
                fontWeight: 'bolder',
                fontFamily: 'fontkhachhang',
                mt: -1,
              }}
            >
              {currentTable && <>Phiếu thanh toán - {currentTable.name}</>}
            </Typography>
            <Grid container spacing={3}>
              <Grid item xs={7} md={7} lg={7}>
                <Stack
                  direction="row"
                  alignItems="center"
                  justifyContent="flex-start"
                  sx={{
                    my: 2,
                  }}
                >
                  <Iconify icon={'material-symbols:frame-person-sharp'} />
                  <Typography
                    variant="transition-modal-description"
                    sx={{
                      ml: 1,
                      fontSize: '14px',
                      color: '#333333',
                      fontWeight: 'bolder',
                    }}
                  >
                    Khách lẻ
                  </Typography>
                </Stack>
                <Box
                  sx={{
                    overflow: 'auto',
                    position: 'relative',
                    maxHeight: '580px',
                    minHeight: '580px',
                  }}
                >
                  <TableContainer component={Paper}>
                    <Table
                      sx={{ minWidth: '50%' }}
                      aria-label="customized table"
                    >
                      <TableHead>
                        <TableRow>
                          <StyledTableCell align="center">#</StyledTableCell>
                          <StyledTableCell align="center">Món</StyledTableCell>
                          <StyledTableCell align="center">SL</StyledTableCell>
                          <StyledTableCell align="right">Giá</StyledTableCell>
                          <StyledTableCell align="right">
                            Thành tiền
                          </StyledTableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {tempBillData &&
                          tempBillData.map((orderItem, index) => (
                            <StyledTableRow key={index + 1}>
                              <StyledTableCell
                                align="center"
                                component="th"
                                scope="row"
                              >
                                {index + 1}
                              </StyledTableCell>
                              <StyledTableCell align="center">
                                {orderItem.productTitle}
                              </StyledTableCell>
                              <StyledTableCell align="center">
                                {orderItem.quantity}
                              </StyledTableCell>
                              <StyledTableCell align="right">
                                {Helper.formatCurrencyToVND(orderItem.price)}
                              </StyledTableCell>
                              <StyledTableCell
                                sx={{ paddingRight: '5px !important' }}
                                align="right"
                              >
                                {Helper.formatCurrencyToVND(
                                  orderItem.price * orderItem.quantity
                                )}
                              </StyledTableCell>
                            </StyledTableRow>
                          ))}
                      </TableBody>
                    </Table>
                  </TableContainer>
                </Box>
              </Grid>
              <Grid
                item
                xs={5}
                md={5}
                lg={5}
                sx={{ textAlign: 'justify', mt: 6 }}
              >
                <Stack
                  direction="row"
                  alignItems="center"
                  sx={{ mb: 2 }}
                  justifyContent="flex-end"
                >
                  <Typography
                    variant="transition-modal-description"
                    sx={{
                      mr: 1,
                      fontSize: '14px',
                      color: '#333333',
                      fontWeight: 'normal',
                    }}
                  >
                    {new Date().toLocaleString()}
                  </Typography>
                  <Iconify icon={'uil:calender'} />
                  <Iconify icon={'mdi:alarm-clock'} />
                </Stack>
                <Stack
                  direction="row"
                  alignItems="center"
                  sx={{ mb: 2 }}
                  justifyContent="space-between"
                >
                  <Typography
                    variant="transition-modal-description"
                    sx={{
                      fontSize: '17px',
                      color: '#333333',
                      fontWeight: 'normal',
                    }}
                  >
                    Tổng tiền hàng:
                  </Typography>
                  <NumericFormat
                    value={orderItemData.totalAmount}
                    thousandSeparator=","
                    customInput={TextField}
                    variant="filled"
                    InputProps={{
                      disableUnderline: true,
                      readOnly: true,
                    }}
                    sx={{
                      input: {
                        color: 'red',
                        backgroundColor: 'white',
                        fontSize: '17px',
                        padding: ' 0px',
                        fontWeight: 'bold',
                        textAlign: 'right',
                        width: '150px',
                      },
                    }}
                  />
                </Stack>
                <Stack
                  direction="row"
                  alignItems="center"
                  sx={{ mb: 2 }}
                  justifyContent="space-between"
                >
                  <Typography
                    variant="transition-modal-description"
                    sx={{
                      fontSize: '17px',
                      color: '#333333',
                      fontWeight: 'normal',
                    }}
                  >
                    Giảm Giá (%):
                  </Typography>
                  <NumericFormat
                    value=""
                    isAllowed={(values) => {
                      const { formattedValue, floatValue } = values
                      return (
                        formattedValue === '' ||
                        (floatValue > 0 && floatValue <= 100)
                      )
                    }}
                    // allowNegative={false}
                    thousandSeparator=","
                    customInput={Input}
                    // inputProps={{ maxLength: 3, max: 100 }}
                    placeholder="0%"
                    sx={{
                      ml: 2,
                      input: {
                        color: 'black',
                        fontWeight: 'bold',
                        fontSize: '17px',
                        textAlign: 'right',
                        width: '150px',
                      },
                    }}
                    onInput={handleChangeDiscount}
                  />
                </Stack>
                <Stack
                  direction="row"
                  alignItems="center"
                  sx={{ mb: 2 }}
                  justifyContent="space-between"
                >
                  <Typography
                    variant="transition-modal-description"
                    sx={{
                      fontSize: '17px',
                      color: '#333333',
                      fontWeight: 'normal',
                    }}
                  >
                    Phí dịch vụ (%):
                  </Typography>
                  <NumericFormat
                    value=""
                    isAllowed={(values) => {
                      const { formattedValue, floatValue } = values
                      return (
                        formattedValue === '' ||
                        (floatValue > 0 && floatValue <= 100)
                      )
                    }}
                    thousandSeparator=","
                    customInput={Input}
                    placeholder="0%"
                    sx={{
                      ml: 2,
                      input: {
                        color: 'black',
                        fontWeight: 'bold',
                        fontSize: '17px',
                        textAlign: 'right',
                        width: '150px',
                      },
                    }}
                    onChange={handleChangeFees}
                    // onValueChange={handleChangeFees}
                  />
                </Stack>

                <Stack
                  direction="row"
                  alignItems="center"
                  sx={{ mb: 2 }}
                  justifyContent="space-between"
                >
                  <Typography
                    variant="transition-modal-description"
                    sx={{
                      fontSize: '17px',
                      color: '#333333',
                      fontWeight: 'normal',
                    }}
                  >
                    Tổng tiền :
                  </Typography>
                  <NumericFormat
                    value={paymentData.totalAmount}
                    thousandSeparator=","
                    customInput={TextField}
                    variant="filled"
                    InputProps={{
                      disableUnderline: true,
                      readOnly: true,
                    }}
                    sx={{
                      input: {
                        color: 'red',
                        backgroundColor: 'white',
                        fontSize: '17px',
                        padding: ' 0px',
                        fontWeight: 'bold',
                        textAlign: 'right',
                        width: '150px',
                      },
                    }}
                  />
                </Stack>

                <Stack
                  direction="row"
                  alignItems="center"
                  sx={{ mb: 2 }}
                  justifyContent="space-between"
                >
                  <Typography
                    variant="transition-modal-description"
                    sx={{
                      fontSize: '17px',
                      color: '#333333',
                      fontWeight: 'normal',
                    }}
                  >
                    Tiền mặt thanh toán:
                  </Typography>
                  <NumericFormat
                    value=""
                    thousandSeparator=","
                    customInput={Input}
                    placeholder="0"
                    sx={{
                      ml: 2,
                      input: {
                        color: 'black',
                        fontWeight: 'bold',
                        fontSize: '17px',
                        textAlign: 'right',
                        width: '150px',
                      },
                    }}
                    onChange={handleCashMoney}
                    // onValueChange={({ floatValue }) => {
                    //   return setState({
                    //     ...state,
                    //     moneyReceived: floatValue,
                    //     changeMoney: floatValue - totalAmount,
                    //   })
                    // }}
                  />
                </Stack>
                <Stack
                  direction="row"
                  alignItems="center"
                  sx={{ mb: 2 }}
                  justifyContent="space-between"
                >
                  <Typography
                    variant="transition-modal-description"
                    sx={{
                      fontSize: '17px',
                      color: '#333333',
                      fontWeight: 'normal',
                    }}
                  >
                    Khách chuyển khoản:
                  </Typography>
                  <NumericFormat
                    value=""
                    thousandSeparator=","
                    customInput={Input}
                    placeholder="0"
                    sx={{
                      ml: 2,
                      input: {
                        color: 'black',
                        fontWeight: 'bold',
                        fontSize: '17px',
                        textAlign: 'right',
                        width: '150px',
                      },
                    }}
                    onChange={handleTransferMoney}
                    // onValueChange={({ floatValue }) => {
                    //   return setState({
                    //     ...state,
                    //     moneyReceived: floatValue,
                    //     changeMoney: floatValue - totalAmount,
                    //   })
                    // }}
                  />
                </Stack>

                <Divider sx={{ mb: 2 }} />
                <Stack
                  direction="row"
                  alignItems="center"
                  sx={{ mb: 2 }}
                  justifyContent="space-between"
                >
                  <Typography
                    variant="transition-modal-description"
                    sx={{
                      mr: 1,
                      fontSize: '17px',
                      color: '#333333',
                      fontWeight: 'normal',
                    }}
                  >
                    Tiền thừa trả khách:
                  </Typography>
                  <NumericFormat
                    value={paymentData.changeMoney}
                    thousandSeparator=","
                    customInput={TextField}
                    variant="filled"
                    InputProps={{
                      disableUnderline: true,
                      readOnly: true,
                    }}
                    sx={{
                      input: {
                        color: 'green',
                        backgroundColor: 'white',
                        fontSize: '17px',
                        padding: ' 0px',
                        fontWeight: 'bold',
                        textAlign: 'right',
                        width: '150px',
                      },
                    }}
                  />
                </Stack>

                <Stack
                  direction="row"
                  alignItems="center"
                  sx={{ mb: 2 }}
                  justifyContent="space-between"
                >
                  <LoadingButton
                    loading={loading}
                    variant="contained"
                    sx={{
                      color: 'white',
                      backgroundColor: '#B55D28',
                      border: '1px solid #B55D28',
                      width: 200,
                      margin: '0 10%',
                      '&:hover': {
                        backgroundColor: 'white',
                        color: '#d0181b',
                      },
                    }}
                    onClick={handlePayTemp}
                  >
                    <Iconify
                      icon={'ri:money-dollar-circle-line'}
                      width="25px"
                      height={50}
                      sx={{
                        mr: 1,
                      }}
                    />
                    In tạm
                  </LoadingButton>
                  <LoadingButton
                    loading={loading}
                    variant="contained"
                    sx={{
                      color: 'white',
                      backgroundColor: '#28B44F',
                      border: '1px solid #28B44F',
                      width: 250,
                      '&:hover': {
                        backgroundColor: 'white',
                        color: '#d0181b',
                      },
                    }}
                    onClick={handlePay}
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
              </Grid>
            </Grid>
          </Box>
        </Slide>
      </Modal>

      {currentTable && (
        <iframe
          title="helo"
          id="billPapper"
          style={{ height: '0px', width: '0px', position: 'absolute' }}
        >
          <div id="billPapperContent">
            <div
              className="invoice-POS"
              style={{ textAlign: 'center', margin: 0, padding: 0 }}
            >
              <center id="top">
                <img
                  src={Images.LOADING_LOGO}
                  alt="Logo"
                  height="50"
                  style={{ margin: 0 }}
                />
                <h2 style={{ margin: 0 }}>White Lotus Restaurant</h2>
              </center>

              <div id="mid">
                <div className="info">
                  <p>
                    ĐC : 28 Nguyễn Tri Phương, phường Phú Nhuận, thành phố Huế
                    <br />
                    Email : whitelotusrestaurant@gmail.com
                    <br />
                    Điện thoại: 0961133348
                    <br />
                  </p>
                </div>
              </div>
              <Divider />
              <h3 style={{ margin: 0, padding: 0 }}>HÓA ĐƠN BÁN HÀNG</h3>
              <h3 style={{ margin: 0 }}>{currentTable.name}</h3>

              <table style={{ width: '100%' }}>
                <tbody>
                  <tr style={{ fontSize: '13px' }}>
                    <td>
                      <p>Ngày: {Helper.formatDate(new Date())}</p>
                    </td>
                    <td style={{ textAlign: 'right' }}>
                      <p>Số: {currentTable.orderId}</p>
                    </td>
                  </tr>
                  <tr style={{ fontSize: '13px' }}>
                    <td colSpan={2}>
                      <p>Giờ: {new Date().toLocaleTimeString()}</p>
                    </td>
                  </tr>
                  <tr style={{ fontSize: '13px' }}>
                    <td colSpan={2}>
                      <p>Thu ngân: {fullName}</p>
                    </td>
                  </tr>
                </tbody>
              </table>
              <table
                style={{
                  width: '100%',
                  border: '1px dashed black',
                  borderCollapse: 'collapse',
                }}
                className="tableBill"
              >
                <tbody>
                  <tr>
                    <th style={{ border: '1px dashed black' }}>
                      <p style={{ textAlign: 'left' }}>MẶT HÀNG</p>
                    </th>
                    <th style={{ border: '1px dashed black' }}>
                      <p style={{ textAlign: 'center' }}>SL</p>
                    </th>
                    <th
                      style={{ textAlign: 'right', border: '1px dashed black' }}
                    >
                      <p>GIÁ</p>
                    </th>
                    <th
                      style={{ textAlign: 'right', border: '1px dashed black' }}
                    >
                      <p>TỔNG</p>
                    </th>
                  </tr>
                  {tempBillData &&
                    tempBillData.map((item, index) => (
                      <tr key={index}>
                        <td
                          style={{
                            textAlign: 'left',
                            border: '1px dashed black',
                          }}
                        >
                          <p>{item.productTitle}</p>
                        </td>
                        <td
                          style={{
                            textAlign: 'center',
                            border: '1px dashed black',
                          }}
                        >
                          <p>{item.quantity}</p>
                        </td>
                        <td
                          style={{
                            textAlign: 'right',
                            border: '1px dashed black',
                          }}
                        >
                          <p>{item.price.toLocaleString()}</p>
                        </td>
                        <td
                          style={{
                            textAlign: 'right',
                            border: '1px dashed black',
                          }}
                        >
                          <p>{item.amount.toLocaleString()}</p>
                        </td>
                      </tr>
                    ))}
                </tbody>
              </table>
              <table style={{ width: '100%' }}>
                <tbody>
                  <tr>
                    <td>
                      <h4>Giảm giá:</h4>
                    </td>
                    <td style={{ textAlign: 'right' }}>
                      <h4>
                        {Helper.formatCurrencyToVND(paymentData.discountMoney)}
                      </h4>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <h4>Phụ thu:</h4>
                    </td>
                    <td style={{ textAlign: 'right' }}>
                      <h4>
                        {Helper.formatCurrencyToVND(paymentData.feesMoney)}
                      </h4>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <h3>Tổng tiền:</h3>
                    </td>
                    <td style={{ textAlign: 'right' }}>
                      <h3>
                        {Helper.formatCurrencyToVND(paymentData.totalAmount)}
                      </h3>
                    </td>
                  </tr>
                </tbody>
              </table>

              <div id="bot">
                <div id="legalcopy">
                  <p className="legal">
                    <strong>Cảm ơn Quý Khách, hẹn gặp lại!</strong>&nbsp;
                  </p>
                </div>
              </div>
            </div>
          </div>
        </iframe>
      )}

      {currentTable && (
        <iframe
          title="helo"
          id="billPapperTemp"
          style={{ height: '0px', width: '0px', position: 'absolute' }}
        >
          <div id="billPapperTempContent">
            <div
              className="invoice-POS"
              style={{ textAlign: 'center', margin: 0, padding: 0 }}
            >
              <center id="top">
                <img
                  src={Images.LOADING_LOGO}
                  alt="Logo"
                  height="50"
                  style={{ margin: 0 }}
                />
                <h2 style={{ margin: 0 }}>White Lotus Restaurant</h2>
              </center>

              <div id="mid">
                <div className="info">
                  <p>
                    ĐC : 28 Nguyễn Tri Phương, phường Phú Nhuận, thành phố Huế
                    <br />
                    Email : whitelotusrestaurant@gmail.com
                    <br />
                    Điện thoại: 0961133348
                    <br />
                  </p>
                </div>
              </div>
              <Divider />
              <h3 style={{ margin: 0, padding: 0 }}>HÓA ĐƠN IN TẠM</h3>
              <h3 style={{ margin: 0 }}>{currentTable.name}</h3>

              <table style={{ width: '100%' }}>
                <tbody>
                  <tr style={{ fontSize: '13px' }}>
                    <td>
                      <p>Ngày: {Helper.formatDate(new Date())}</p>
                    </td>
                    <td style={{ textAlign: 'right' }}>
                      <p>Số: {currentTable.orderId}</p>
                    </td>
                  </tr>
                  <tr style={{ fontSize: '13px' }}>
                    <td colSpan={2}>
                      <p>Giờ: {new Date().toLocaleTimeString()}</p>
                    </td>
                  </tr>
                  <tr style={{ fontSize: '13px' }}>
                    <td colSpan={2}>
                      <p>Thu ngân: {fullName}</p>
                    </td>
                  </tr>
                </tbody>
              </table>
              <table
                style={{
                  width: '100%',
                  border: '1px dashed black',
                  borderCollapse: 'collapse',
                }}
                className="tableBill"
              >
                <tbody>
                  <tr>
                    <th style={{ border: '1px dashed black' }}>
                      <p style={{ textAlign: 'left' }}>MẶT HÀNG</p>
                    </th>
                    <th style={{ border: '1px dashed black' }}>
                      <p style={{ textAlign: 'center' }}>SL</p>
                    </th>
                    <th
                      style={{ textAlign: 'right', border: '1px dashed black' }}
                    >
                      <p>GIÁ</p>
                    </th>
                    <th
                      style={{ textAlign: 'right', border: '1px dashed black' }}
                    >
                      <p>TỔNG</p>
                    </th>
                  </tr>
                  {tempBillData &&
                    tempBillData.map((item, index) => (
                      <tr key={index}>
                        <td
                          style={{
                            textAlign: 'left',
                            border: '1px dashed black',
                          }}
                        >
                          <p>{item.productTitle}</p>
                        </td>
                        <td
                          style={{
                            textAlign: 'center',
                            border: '1px dashed black',
                          }}
                        >
                          <p>{item.quantity}</p>
                        </td>
                        <td
                          style={{
                            textAlign: 'right',
                            border: '1px dashed black',
                          }}
                        >
                          <p>{item.price.toLocaleString()}</p>
                        </td>
                        <td
                          style={{
                            textAlign: 'right',
                            border: '1px dashed black',
                          }}
                        >
                          <p>{item.amount.toLocaleString()}</p>
                        </td>
                      </tr>
                    ))}
                </tbody>
              </table>
              <table style={{ width: '100%' }}>
                <tbody>
                  <tr>
                    <td>
                      <h4>Giảm giá:</h4>
                    </td>
                    <td style={{ textAlign: 'right' }}>
                      <h4>
                        {Helper.formatCurrencyToVND(paymentData.discountMoney)}
                      </h4>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <h4>Phụ thu:</h4>
                    </td>
                    <td style={{ textAlign: 'right' }}>
                      <h4>
                        {Helper.formatCurrencyToVND(paymentData.feesMoney)}
                      </h4>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <h3>Tổng tiền:</h3>
                    </td>
                    <td style={{ textAlign: 'right' }}>
                      <h3>
                        {Helper.formatCurrencyToVND(paymentData.totalAmount)}
                      </h3>
                    </td>
                  </tr>
                </tbody>
              </table>

              <div id="bot">
                <div id="legalcopy">
                  <p className="legal">
                    <strong>Cảm ơn Quý Khách, hẹn gặp lại!</strong>&nbsp;
                  </p>
                </div>
              </div>
            </div>
          </div>
        </iframe>
      )}
    </>
  )
}
