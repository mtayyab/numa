// Final JWT token testing
const testJWTFinal = async () => {
  try {
    console.log('🔍 Final JWT Token Testing...\n');
    
    // Get a fresh token
    const loginResponse = await fetch('http://localhost:8080/api/v1/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        email: 'test@restaurant.com',
        password: 'password123'
      })
    });
    
    if (!loginResponse.ok) {
      console.log('❌ Login failed');
      return;
    }
    
    const loginData = await loginResponse.json();
    const token = loginData.accessToken;
    
    console.log('1. Token received:', token.substring(0, 50) + '...');
    
    // Test the /auth/me endpoint
    console.log('\n2. Testing /auth/me endpoint...');
    const meResponse = await fetch('http://localhost:8080/api/v1/auth/me', {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    
    console.log('   - Response Status:', meResponse.status);
    
    if (meResponse.ok) {
      const userData = await meResponse.json();
      console.log('✅ /auth/me successful');
      console.log('   - User Data:', userData);
    } else {
      const errorData = await meResponse.json();
      console.log('❌ /auth/me failed');
      console.log('   - Error:', errorData);
    }
    
    // Test other endpoints
    console.log('\n3. Testing other endpoints...');
    
    // Test restaurant endpoint
    const restaurantResponse = await fetch(`http://localhost:8080/api/v1/restaurants/${loginData.restaurantId}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    
    console.log('   - Restaurant endpoint status:', restaurantResponse.status);
    
    if (restaurantResponse.ok) {
      console.log('✅ Restaurant endpoint working');
    } else {
      const errorData = await restaurantResponse.json();
      console.log('❌ Restaurant endpoint failed:', errorData.message);
    }
    
  } catch (error) {
    console.error('❌ Test failed:', error.message);
  }
};

// Run the test
testJWTFinal();
