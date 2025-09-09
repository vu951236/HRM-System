import { useAuth } from '../../context/AuthContext.jsx';

export const usePermission = () => {
    const { user } = useAuth();

    const isAdmin = user?.role === 'admin';
    const isHrNormal = user?.role === 'hr' && user?.positionName !== 'Head of Department';
    const isHrHod = user?.role === 'hr' && user?.positionName === 'Head of Department';
    const isStaffHod = user?.role === 'staff' && user?.positionName === 'Head of Department';

    const canApproveOrReject = (item) => {
        if (item.isDelete || item.status !== 'pending') return false;

        return (
            isAdmin ||
            (isHrNormal && item.employeePositionName === 'Staff' && item.userId !== Number(user.userId)) ||
            (isHrHod && item.userId !== Number(user.userId)) ||
            (isStaffHod && item.employeePositionName === 'Staff' && item.departmentId === user.departmentId)
        );
    };

    const canSeeActionColumn = isAdmin || isHrNormal || isHrHod || isStaffHod;

    return { canApproveOrReject, canSeeActionColumn, isAdmin };
};
