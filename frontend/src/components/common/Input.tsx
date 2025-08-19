import React, { forwardRef } from 'react';
import '../../css/input.css';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
}

export const Input = forwardRef<HTMLInputElement, InputProps>(
  ({ label, error, className = '', ...props }, ref) => {
    const inputClass = `input-field ${error ? 'input-error' : ''} ${className}`;

    return (
      <div className="input-wrapper">
        {label && <label className="input-label">{label}</label>}
        <input ref={ref} className={inputClass} {...props} />
        {error && <p className="input-error-message">{error}</p>}
      </div>
    );
  }
);

Input.displayName = 'Input';
