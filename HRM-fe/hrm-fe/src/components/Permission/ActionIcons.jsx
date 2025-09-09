import React from 'react';

const ActionIcons = ({ item, canApproveOrReject, isAdmin, onApprove, onReject, onDelete, onRestore }) => {
    if (!canApproveOrReject(item) && !isAdmin) return null;

    return (
        <td className="action-icons">
            {canApproveOrReject(item) && (
                <>
                    <i
                        className="fa fa-check"
                        style={{ cursor: 'pointer', color: 'green', marginRight: '10px' }}
                        title="Phê duyệt"
                        onClick={() => onApprove(item.id)}
                    />
                    <i
                        className="fa fa-times"
                        style={{ cursor: 'pointer', color: 'red', marginRight: '10px' }}
                        title="Từ chối"
                        onClick={() => onReject(item.id)}
                    />
                </>
            )}

            {isAdmin && !item.isDelete && (
                <i
                    className="fa fa-trash"
                    style={{ cursor: 'pointer', color: 'red' }}
                    title="Xóa mềm"
                    onClick={() => {
                        if (window.confirm('Bạn có chắc muốn xóa mục này?')) {
                            onDelete(item.id);
                        }
                    }}
                />
            )}

            {isAdmin && item.isDelete && (
                <i
                    className="fa fa-undo"
                    style={{ cursor: 'pointer', color: 'green' }}
                    title="Khôi phục"
                    onClick={() => {
                        if (window.confirm('Bạn có chắc muốn khôi phục mục này?')) {
                            onRestore(item.id);
                        }
                    }}
                />
            )}
        </td>
    );
};

export default ActionIcons;
