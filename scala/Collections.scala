import scala.collection.mutable

// ============================================================
// IMMUTABLE COLLECTIONS
// ============================================================

// --- IMMUTABLE LIST ---
val nums     = List(1, 2, 3, 4, 5)
val prepended = 0 :: nums
val appended  = nums :+ 6
val headTail  = (nums.head, nums.tail)
val mapped    = nums.map(_ * 2)
val filtered  = nums.filter(_ % 2 == 0)
val folded    = nums.foldLeft(0)(_ + _)
val zipped    = nums.zip(List("a", "b", "c", "d", "e"))
val taken     = nums.take(3)
val dropped   = nums.drop(3)
val flattened = List(List(1, 2), List(3, 4)).flatten

// --- IMMUTABLE VECTOR ---
val vec      = Vector(10, 20, 30, 40)
val vecAt    = vec(2)
val vecPre   = 0 +: vec
val vecApp   = vec :+ 50
val vecUpd   = vec.updated(1, 99)

// --- IMMUTABLE SET ---
val setA     = Set(1, 2, 3, 4)
val setB     = Set(3, 4, 5, 6)
val union    = setA | setB
val inter    = setA & setB
val diff     = setA &~ setB
val hasTwo   = setA.contains(2)
val setAdded = setA + 99
val setRem   = setA - 2

// --- IMMUTABLE MAP ---
val scores   = Map("alice" -> 90, "bob" -> 75, "carol" -> 85)
val getAlice = scores.get("alice")
val getDave  = scores.get("dave")
val withDave = scores + ("dave" -> 70)
val withoutB = scores - "bob"
val bumped   = scores.map((k, v) => k -> (v + 5))
val highScor = scores.filter((_, v) => v >= 85)
val keys     = scores.keys.toList
val vals     = scores.values.toList

// --- RANGE ---
val r1       = 1 to 5
val r2       = 1 until 5
val r3       = 1 to 10 by 2
val rList    = r1.toList

// ============================================================
// MUTABLE COLLECTIONS
// ============================================================

@main def run(): Unit =
  // IMMUTABLE LIST
  println(nums)
  println(prepended)
  println(appended)
  println(headTail)
  println(mapped)
  println(filtered)
  println(folded)
  println(zipped)
  println(taken)
  println(dropped)
  println(flattened)

  // IMMUTABLE VECTOR
  println(vec)
  println(vecAt)
  println(vecPre)
  println(vecApp)
  println(vecUpd)

  // IMMUTABLE SET
  println(setA)
  println(union)
  println(inter)
  println(diff)
  println(hasTwo)
  println(setAdded)
  println(setRem)

  // IMMUTABLE MAP
  println(scores)
  println(getAlice)
  println(getDave)
  println(withDave)
  println(withoutB)
  println(bumped)
  println(highScor)
  println(keys)
  println(vals)

  // RANGE
  println(r1.toList)
  println(r2.toList)
  println(r3.toList)
  println(rList)

  // MUTABLE ARRAYBUFFER
  val buf = mutable.ArrayBuffer(1, 2, 3)
  buf += 4
  buf.prepend(0)
  buf.insert(2, 99)
  buf.remove(2)
  buf(0) = 10
  println(buf)
  buf.foreach(println)

  // MUTABLE LISTBUFFER
  val lbuf = mutable.ListBuffer("a", "b", "c")
  lbuf += "d"
  lbuf.prepend("z")
  lbuf -= "b"
  val immutableList = lbuf.toList
  println(lbuf)
  println(immutableList)

  // MUTABLE MAP
  val mmap = mutable.Map("x" -> 1, "y" -> 2)
  mmap("z") = 3
  mmap.update("x", 99)
  mmap.remove("y")
  val mget = mmap.getOrElse("z", -1)
  println(mmap)
  println(mget)

  // MUTABLE SET
  val mset = mutable.Set(10, 20, 30)
  mset += 40
  mset -= 20
  val mhas = mset.contains(10)
  println(mset)
  println(mhas)

  // ARRAY (fixed size, mutable elements)
  val arr    = Array(1, 2, 3, 4, 5)
  arr(0)     = 100
  val arrMap = arr.map(_ * 2)
  println(arr.toList)
  println(arrMap.toList)
