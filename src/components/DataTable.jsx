import React from 'react';

const DataTable = ({ columns, data, pagination, setPagination, isLoading }) => (
  <div className="bg-white rounded-lg shadow overflow-x-auto">
    <table className="min-w-full">
      <thead>
        <tr className="bg-primary text-white">
          {columns.map(col => (
            <th key={col.header} className="p-3 text-left">{col.header}</th>
          ))}
        </tr>
      </thead>
      <tbody>
        {data.map((row, index) => (
          <tr key={index} className="border-b var(--borders)">
            {columns.map(col => (
              <td key={col.header} className="p-3">
                {col.render ? col.render(row[col.accessor], row) : row[col.accessor]}
              </td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
    <div className="p-4 flex justify-between items-center">
      <button
        className="px-4 py-2 bg-primary text-white rounded disabled:bg-gray-400"
        disabled={pagination.page === 1 || isLoading}
        onClick={() => setPagination(prev => ({ ...prev, page: prev.page - 1 }))}
        aria-label="Previous page"
      >
        Previous
      </button>
      <span>Page {pagination.page} of {pagination.totalPages}</span>
      <button
        className="px-4 py-2 bg-primary text-white rounded disabled:bg-gray-400"
        disabled={pagination.page === pagination.totalPages || isLoading}
        onClick={() => setPagination(prev => ({ ...prev, page: prev.page + 1 }))}
        aria-label="Next page"
      >
        Next
      </button>
    </div>
    {isLoading && <div className="p-4 text-center">Loading...</div>}
  </div>
);

export default DataTable; 