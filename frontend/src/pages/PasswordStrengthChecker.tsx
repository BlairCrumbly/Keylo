import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/passwordStrengthChecker.css';

export const PasswordStrengthChecker: React.FC = () => {
  const [password, setPassword] = useState('');
  const [strength, setStrength] = useState('');
  const navigate = useNavigate();

  const evaluateStrength = (pwd: string): string => {
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

  return (
    <div className="checker-page">
      <div className="checker-container">
        <h1 className="checker-title"> Password Strength Checker</h1>
        <p className="checker-subtitle">Test your password against strength standards.</p>

        <input
          type="password"
          value={password}
          onChange={handleChange}
          className="checker-input"
          placeholder="Enter your password..."
        />

        {password && (
          <div className={`strength-indicator ${strength.toLowerCase()}`}>
            Strength: <strong>{strength}</strong>
          </div>
        )}

        <button
          onClick={() => navigate('/')}
          className="back-button"
        >
          ← Back to Dashboard
        </button>
      </div>
    </div>
  );
};
