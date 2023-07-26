import React, { useState, useEffect } from 'react'
import {
  Grid,
  Container,
  Typography,
  Paper,
  Stack,
  Divider,
  TextField,
  Button,
  Fade,
  Backdrop,
  Box,
  Modal,
  useTheme,
  styled,
} from '@mui/material'

import Iconify from 'components/Iconify'
import AppOrderItemList from './AppOrderItemList'
import AppTableItemCooking from './AppTableItemCooking'
import KitchenProductEmpty from './KitchenProductEmpty'
import { useSelector } from 'react-redux'
import KitchenTableEmpty from './KitchenTableEmpty'

const GroupProductAndTableCooking = ({ checkMenu, ...other }) => {
  const kitchenData = useSelector((state) => state.kitchenData.data)
  const orderItemsTable = kitchenData.orderItemsTable
  const orderItemsCooking = kitchenData.orderItemsCooking

  return (
    <Box
      style={{
        height: '94vh',
        // maxHeight: 650,
        // minHeight: 650,
        overflow: 'hidden',
        // padding: '10px',
        paddingRight: '5px',
        paddingBottom: '50px',
        // backgroundColor: '#EFF0F1',
        // borderRadius: '12px',
        overflowY: 'scroll',
      }}
      className="wrapperBoard"
    >
      {checkMenu ? (
        orderItemsCooking.length ? (
          <AppOrderItemList orderItemsCooking={orderItemsCooking} />
        ) : (
          <KitchenProductEmpty />
        )
      ) : orderItemsTable.length ? (
        <AppTableItemCooking orderItemsTable={orderItemsTable} />
      ) : (
        <KitchenTableEmpty />
      )}
    </Box>
  )
}

export default GroupProductAndTableCooking
