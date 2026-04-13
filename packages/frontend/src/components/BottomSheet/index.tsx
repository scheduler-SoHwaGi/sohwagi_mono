import React from 'react';
import { Sheet } from 'react-modal-sheet';
import Delete from '../../assets/images/delete.svg';

interface BottomSheetProps {
  isOpen: boolean;
  onClose: () => void;
  onDelete: () => void;
}

const BottomSheet: React.FC<BottomSheetProps> = ({
  isOpen,
  onClose,
  onDelete,
}) => {
  return (
    <Sheet isOpen={isOpen} onClose={onClose} snapPoints={[0.75, 0.5]}>
      <Sheet.Container
        style={{
          maxHeight: 'min(100px, calc(100% - env(safe-area-inset-top) - 30px))',
          borderRadius: '1.3125rem',
          marginBottom: '44px',
          left: '5%',
          width:'90%'
        }}
      >
        <Sheet.Header />
        <Sheet.Content className={'h-fit'}>
          <Sheet.Scroller>
            <div
              className="flex px-20 items-center gap-[0.31rem] cursor-pointer h-fit"
              onClick={onDelete}
            >
              <img src={Delete} alt="delete icon" className="w-35 h-35" />
              <span className="text-Grey_04 body_03">삭제하기</span>
            </div>
          </Sheet.Scroller>
        </Sheet.Content>
      </Sheet.Container>

      <Sheet.Backdrop onTap={onClose} />
    </Sheet>
  );
};

export default BottomSheet;
