import React from 'react'
import { Typography, Divider, Button, Fade, Box, Modal } from '@mui/material'

import Iconify from 'components/Iconify'

import Helper from 'utils/Helper'
import { useSelector } from 'react-redux'
import { BillStatus } from 'constants/BillStatus'
import Images from 'constants/images'

export default function ModalDetailBill({
  openModalBillHistory,
  closeModalBillHistory,
}) {
  const baseData = useSelector((state) => state.baseData.data)
  const currentBill = baseData.currentBill
  const currenrOrderitemsBill = baseData.currenrOrderitemsBill

  const handleRePrintBill = () => {
    setTimeout(() => {
      const content = document.getElementById('rePrintBillPapperContent')
      const pri = document.getElementById('rePrintBillPapper').contentWindow
      pri.document.open()
      pri.document.write(content.innerHTML)
      pri.document.close()
      pri.focus()
      pri.print()
    }, 0)
  }

  return (
    <>
      <Modal
        aria-labelledby="transition-modal-title"
        aria-describedby="transition-modal-description"
        open={openModalBillHistory}
        onClose={closeModalBillHistory}
        closeAfterTransition
        disableAutoFocus
      >
        <Fade in={openModalBillHistory}>
          <Box
            sx={{
              position: 'absolute',
              top: '50%',
              left: '50%',
              transform: 'translate(-50%, -50%)',
              width: '1200px',
              bgcolor: 'background.paper',
              border: '2px solid #d8d8df',
              borderRadius: '12px',
              boxShadow: 24,
              p: 3,
              textAlign: 'center',
            }}
          >
            <Iconify
              icon={'material-symbols:close'}
              position="absolute"
              top="-10px"
              right="-10px"
              bgcolor="white"
              color="#5A5A72"
              borderRadius="50%"
              border="1px solid #d8d8df"
              width={36}
              padding={0.6}
              cursor="pointer"
              boxShadow="rgba(0, 0, 0, 0.2) 0px 2px 12px 0px"
              sx={{
                '&:hover': {
                  color: '#131318',
                  bgcolor: '#EBEBEF',
                  border: '1px solid #B9B9c6',
                },
              }}
              onClick={closeModalBillHistory}
            />

            <div className="row">
              <Typography
                id="transition-modal-title"
                variant="h6"
                component="h2"
                sx={{
                  color: '#53382c',
                  textTransform: 'uppercase',
                  fontWeight: 'bolder',
                  fontFamily: 'fontkhachhang',
                }}
              >
                Chi tiết hoá đơn {currentBill.tableName}
              </Typography>
            </div>

            <div className="col-12 row row-bill-detail">
              <div className="col-2 d-flex">Số: {currentBill.id}</div>
              <div className="col-4 d-flex justify-content-center">
                Ngày: {currentBill.createdAt}
              </div>
              <div className="col-3 d-flex justify-content-center">
                Nhân viên: {currentBill.staffName}
              </div>
              <div className="col-3 d-flex justify-content-end">
                Trạng thái:{' '}
                {currentBill.status === BillStatus.ORDERING.status
                  ? BillStatus.ORDERING.statusValue
                  : currentBill.status === BillStatus.TEMPORARY.status
                  ? BillStatus.TEMPORARY.statusValue
                  : currentBill.status === BillStatus.CANCEL.status
                  ? BillStatus.CANCEL.statusValue
                  : BillStatus.PAID.statusValue}
              </div>
            </div>
            <div
              className="col-12 row row-bill-detail"
              style={{ overflow: 'auto', maxHeight: '375px', padding: 0 }}
            >
              <table className="table table-striped table-hover table-border table-bill-detail">
                <thead style={{ color: '#fff' }}>
                  <tr className="">
                    <th className="text-start">Sản phẩm</th>
                    <th className="text-center">Số lượng</th>
                    <th className="text-end">Giá</th>
                    <th className="text-end">Thành tiền</th>
                  </tr>
                </thead>
                <tbody>
                  {currenrOrderitemsBill &&
                    currenrOrderitemsBill.map((item) => (
                      <tr key={item.id}>
                        <td className="text-start">{item.productTitle}</td>
                        <td className="text-center">{item.quantity}</td>
                        <td className="text-end">
                          {Helper.formatCurrencyToVND(item.price)}
                        </td>
                        <td className="text-end">
                          {Helper.formatCurrencyToVND(item.amount)}
                        </td>
                      </tr>
                    ))}
                </tbody>
              </table>
            </div>
            <div className="col-12 row row-bill-detail">
              <div className="col-3 col-bill-detail">
                Tổng tiền: {Helper.formatCurrencyToVND(currentBill.orderPrice)}
              </div>
              <div
                className="col-3 col-bill-detail"
                style={{ color: '#f65a6d', fontSize: 16 }}
              >
                Phụ thu: {currentBill.chargePercent}% + &nbsp;
                {Helper.formatCurrencyToVND(currentBill.chargeMoney)}
              </div>
              <div
                className="col-3 col-bill-detail"
                style={{ color: '#0ee12a', fontSize: 16 }}
              >
                Giảm giá: {currentBill.discountPercent}% - &nbsp;
                {Helper.formatCurrencyToVND(currentBill.discountMoney)}
              </div>
              <div
                className="col-3 col-bill-detail justify-content-end"
                style={{ color: '#f6d551', fontSize: 24 }}
              >
                Chốt đơn: &nbsp;
                <b>{Helper.formatCurrencyToVND(currentBill.totalAmount)}</b>
              </div>
            </div>
            <div className="row d-flex justify-content-center align-items-center mt-2">
              <div
                className="row d-flex justify-content-center align-items-center"
                style={{ width: '200px' }}
              >
                <Button
                  variant="contained"
                  sx={{
                    color: 'white',
                    backgroundColor: '#4B4086',
                    border: '1px solid #4B4086',
                    mt: 2,
                    '&:hover': {
                      backgroundColor: 'white',
                      color: '#7266ba',
                    },
                  }}
                  onClick={handleRePrintBill}
                >
                  In Lại Hoá Đơn {currentBill.tableName}
                </Button>
              </div>
            </div>
          </Box>
        </Fade>
      </Modal>

      {currentBill && (
        <iframe
          title="helo"
          id="rePrintBillPapper"
          style={{ height: '0px', width: '0px', position: 'absolute' }}
        >
          <div id="rePrintBillPapperContent">
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
              <h3 style={{ margin: 0 }}>{currentBill.tableName}</h3>

              <table style={{ width: '100%' }}>
                <tbody>
                  <tr style={{ fontSize: '13px' }}>
                    <td>
                      <p>Ngày: {Helper.formatDate(new Date())}</p>
                    </td>
                    <td style={{ textAlign: 'right' }}>
                      <p>Số: {currentBill.id}</p>
                    </td>
                  </tr>
                  <tr style={{ fontSize: '13px' }}>
                    <td colSpan={2}>
                      <p>Giờ: {new Date().toLocaleTimeString()}</p>
                    </td>
                  </tr>
                  <tr style={{ fontSize: '13px' }}>
                    <td colSpan={2}>
                      <p>Thu ngân: {currentBill.staffName}</p>
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
                  {currenrOrderitemsBill &&
                    currenrOrderitemsBill.map((item, index) => (
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
                        {Helper.formatCurrencyToVND(currentBill.discountMoney)}
                      </h4>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <h4>Phụ thu:</h4>
                    </td>
                    <td style={{ textAlign: 'right' }}>
                      <h4>
                        {Helper.formatCurrencyToVND(currentBill.chargeMoney)}
                      </h4>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <h3>Tổng tiền:</h3>
                    </td>
                    <td style={{ textAlign: 'right' }}>
                      <h3>
                        {Helper.formatCurrencyToVND(currentBill.totalAmount)}
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
