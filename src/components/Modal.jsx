import React from 'react';
import { XCircleIcon } from '@heroicons/react/outline';

const Modal = ({ isOpen, onClose, title, children }) => (
  isOpen && (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 w-full max-w-lg">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-bold">{title}</h2>
          <button onClick={onClose} className="text-gray-600 hover:text-gray-800" aria-label="Close modal">
            <XCircleIcon className="h-6 w-6" />
          </button>
        </div>
        {children}
      </div>
    </div>
  )
);

export default Modal; 