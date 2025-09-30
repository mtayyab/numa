// Manual JWT token testing
const jwt = require('jsonwebtoken');

const testJWTManual = async () => {
  try {
    console.log('🔍 Manual JWT Token Testing...\n');
    
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
    
    const loginData = await loginResponse.json();
    const token = loginData.accessToken;
    
    console.log('1. Token received:', token.substring(0, 50) + '...');
    
    // Decode the token without verification
    const decoded = jwt.decode(token);
    console.log('2. Decoded token:', JSON.stringify(decoded, null, 2));
    
    // Check if token is expired
    const now = Math.floor(Date.now() / 1000);
    const exp = decoded.exp;
    const isExpired = now >= exp;
    
    console.log('3. Token expiration check:');
    console.log('   - Current time:', now);
    console.log('   - Expiration time:', exp);
    console.log('   - Is expired:', isExpired);
    console.log('   - Time until expiry:', exp - now, 'seconds');
    
    // Test with different secret keys
    const secrets = [
      'your-super-secret-jwt-key-change-this-in-production',
      'mySecretKey',
      'default-secret-key'
    ];
    
    console.log('\n4. Testing token verification with different secrets:');
    for (const secret of secrets) {
      try {
        const verified = jwt.verify(token, secret);
        console.log(`   ✅ Secret "${secret}" works`);
        console.log('   - Verified payload:', JSON.stringify(verified, null, 2));
        break;
      } catch (error) {
        console.log(`   ❌ Secret "${secret}" failed:`, error.message);
      }
    }
    
  } catch (error) {
    console.error('❌ Test failed:', error.message);
  }
};

// Run the test
testJWTManual();
