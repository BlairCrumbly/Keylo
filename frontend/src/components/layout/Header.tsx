import React from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { Button } from '../common/Button';
import '../../css/button.css'

export const Header: React.FC = () => {
  const { user, logout } = useAuth();

  return (
    <header className="bg-white shadow-md">
      <div className="container mx-auto px-4 py-4 flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-500 text-center">Your Keylo Vault</h1>
        
        {user && (
          <div className="flex items-center gap-4">

            <Button className='logout-btn'
              
              size="sm"
              onClick={logout}
            >
              Logout
            </Button>
          </div>
        )}
      </div>
    </header>
  );
};