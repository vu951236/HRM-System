import React from 'react';

const Pagination = ({ currentPage, totalPages, onChangePage }) => {
    const maxVisiblePages = 10;

    let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = startPage + maxVisiblePages - 1;

    if (endPage > totalPages) {
        endPage = totalPages;
        startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }

    const pages = [];
    for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
    }

    return (
        <div className="pagination">
            <button
                disabled={currentPage === 1}
                onClick={() => onChangePage(currentPage - 1)}
            >
                « Trước
            </button>

            {startPage > 1 && <span>...</span>}

            {pages.map(num => (
                <button
                    key={num}
                    className={currentPage === num ? 'active' : ''}
                    onClick={() => onChangePage(num)}
                >
                    {num}
                </button>
            ))}

            {endPage < totalPages && <span>...</span>}

            {/* Nút sau */}
            <button
                disabled={currentPage === totalPages}
                onClick={() => onChangePage(currentPage + 1)}
            >
                Sau »
            </button>
        </div>
    );
};

export default Pagination;
