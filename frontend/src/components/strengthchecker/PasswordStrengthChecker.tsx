import React, { useState } from 'react';
import './passwordStrengthChecker.css';

export const PasswordStrengthChecker: React.FC = () => {
  const [password, setPassword] = useState('');
  const [strength, setStrength] = useState('');
  const [showPassword, setShowPassword] = useState(false);

  const evaluateStrength = (pwd: string) => {
    let score = 0;
    if (pwd.length >= 8) score++;
    if (/[A-Z]/.test(pwd)) score++;
    if (/[a-z]/.test(pwd)) score++;
    if (/[0-9]/.test(pwd)) score++;
    if (/[^A-Za-z0-9]/.test(pwd)) score++;

    switch (score) {
      case 0:
      case 1:
      case 2:
        return 'Weak';
      case 3:
      case 4:
        return 'Moderate';
      case 5:
        return 'Strong';
      default:
        return '';
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newPwd = e.target.value;
    setPassword(newPwd);
    setStrength(evaluateStrength(newPwd));
  };

  const toggleShowPassword = () => {
    setShowPassword(prev => !prev);
  };

  return (
    <div className="password-checker">
      <label htmlFor="password-input" className="checker-label">Enter a password:</label>
      <div className="checker-input-wrapper">
        <input
          id="password-input"
          type={showPassword ? 'text' : 'password'}
          value={password}
          onChange={handleChange}
          className="checker-input"
          placeholder="Type your password..."
        />
        <button
          type="button"
          onClick={toggleShowPassword}
          className="toggle-button"
        >
          {showPassword ? 'Hide' : 'Show'}
        </button>
      </div>
      {password && (
        <div className={`strength-indicator ${strength.toLowerCase()}`}>
          Strength: {strength}
        </div>
      )}
    </div>
  );
};
