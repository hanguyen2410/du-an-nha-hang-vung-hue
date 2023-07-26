
import { styled } from '@mui/material/styles'

import 'layouts/sweetalert.css'

import Iconify from 'components/Iconify'

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

export default function DeleteOrderItem({ index, item, handleClickDeleteItem }) {
    return (
        <Iconify
            icon={'material-symbols:close'}
            position="absolute"
            top="2px"
            right="10px"
            bgcolor="white"
            color="#5A5A72"
            width={25}
            padding={0.4}
            cursor="pointer"
            sx={{
                '&:hover': {
                    color: '#131318',
                    bgcolor: '#EBEBEF',
                    border: '1px solid #B9B9c6',
                },
            }}
            onClick={() => {
                handleClickDeleteItem(index, item)
            }}
        />
    )
}