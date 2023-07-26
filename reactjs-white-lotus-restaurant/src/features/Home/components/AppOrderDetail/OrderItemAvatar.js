import { Grid } from '@mui/material'

import { styled } from '@mui/material/styles'

import 'layouts/sweetalert.css'

import Box from '@mui/material/Box'

import { COUDINARY } from 'constants/global'

// ----------------------------------------------------------------------

const StyledProductImg = styled('img')({
  top: 0,
  width: '100%',
  height: '100%',
  objectFit: 'cover',
  position: 'absolute',
  borderRadius: '12px',
})

// ----------------------------------------------------------------------

export default function OrderItemAvatar({
  orderItem,
  index,
  handleOpenEditModal,
}) {
  return (
    <Grid item xs={2} md={2} lg={2}>
      <Box
        sx={{
          pt: '100%',
          position: 'relative',
          top: 0,
          // cursor: 'pointer',
        }}
        // onClick={() => {
        //   handleOpenEditModal(orderItem, index)
        // }}
      >
        <StyledProductImg
          alt={orderItem.productTitle}
          src={
            COUDINARY.url +
            '/' +
            COUDINARY.SCALE_IMAGE_120_120 +
            '/' +
            orderItem.productAvatar.fileFolder +
            '/' +
            orderItem.productAvatar.fileName
          }
        />
      </Box>
      {/* <>
                {
                    orderItem.status == OrderItemStatus.NEW.statusValue ? (
                        <Box
                            sx={{
                                pt: '100%',
                                position: 'relative',
                                top: 0,
                                cursor: 'pointer',
                            }}
                            onClick={() => {
                                handleOpenEditModal(orderItem, index)
                            }}
                        >
                            <StyledProductImg
                                alt={orderItem.productTitle}
                                src={
                                    COUDINARY.url +
                                    '/' +
                                    COUDINARY.SCALE_IMAGE_180_180 +
                                    '/' +
                                    orderItem.productAvatar.fileFolder +
                                    '/' +
                                    orderItem.productAvatar.fileName
                                }
                            />
                        </Box>
                    ) : (
                        <Box
                            sx={{
                                pt: '100%',
                                position: 'relative',
                                top: 0,
                            }}
                        >
                            <StyledProductImg
                                alt={orderItem.productTitle}
                                src={
                                    COUDINARY.url +
                                    '/' +
                                    COUDINARY.SCALE_IMAGE_180_180 +
                                    '/' +
                                    orderItem.productAvatar.fileFolder +
                                    '/' +
                                    orderItem.productAvatar.fileName
                                }
                            />
                        </Box>
                    )
                }

            </> */}
    </Grid>
  )
}
